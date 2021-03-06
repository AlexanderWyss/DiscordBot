package wyss.website.discordbot;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;

import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.api.events.EventSubscriber;
import sx.blah.discord.handle.impl.events.ReadyEvent;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.impl.events.guild.channel.message.reaction.ReactionEvent;
import sx.blah.discord.handle.impl.events.guild.voice.user.UserVoiceChannelJoinEvent;
import sx.blah.discord.handle.impl.events.guild.voice.user.UserVoiceChannelLeaveEvent;
import sx.blah.discord.handle.impl.events.guild.voice.user.UserVoiceChannelMoveEvent;
import sx.blah.discord.handle.impl.obj.ReactionEmoji;
import sx.blah.discord.handle.obj.ActivityType;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.handle.obj.IVoiceChannel;
import sx.blah.discord.handle.obj.StatusType;
import sx.blah.discord.util.RequestBuffer;
import wyss.website.discordbot.commands.AdminCommand;
import wyss.website.discordbot.commands.AnnounceCommand;
import wyss.website.discordbot.commands.Command;
import wyss.website.discordbot.commands.DeadminCommand;
import wyss.website.discordbot.commands.FlipACoinCommand;
import wyss.website.discordbot.commands.HelloCommand;
import wyss.website.discordbot.commands.HelpCommand;
import wyss.website.discordbot.commands.JoinCommand;
import wyss.website.discordbot.commands.LeaveCommand;
import wyss.website.discordbot.commands.LockCommand;
import wyss.website.discordbot.commands.MessageCommand;
import wyss.website.discordbot.commands.MusicClearCommand;
import wyss.website.discordbot.commands.MusicContinueCommand;
import wyss.website.discordbot.commands.MusicDurationCommand;
import wyss.website.discordbot.commands.MusicNextCommand;
import wyss.website.discordbot.commands.MusicPanelCommand;
import wyss.website.discordbot.commands.MusicPauseCommand;
import wyss.website.discordbot.commands.MusicPlayCommand;
import wyss.website.discordbot.commands.MusicPlayNextCommand;
import wyss.website.discordbot.commands.MusicPreviousCommand;
import wyss.website.discordbot.commands.MusicQueueCommand;
import wyss.website.discordbot.commands.MusicRemoveCurrentSongCommand;
import wyss.website.discordbot.commands.MusicRepeatCommand;
import wyss.website.discordbot.commands.MusicRepeatSongCommand;
import wyss.website.discordbot.commands.MusicReplaceCommand;
import wyss.website.discordbot.commands.MusicSetVolumeCommand;
import wyss.website.discordbot.commands.RebootCommand;
import wyss.website.discordbot.commands.ShutdownCommand;
import wyss.website.discordbot.commands.ShutupCommand;
import wyss.website.discordbot.commands.playlist.PlaylistAddCommand;
import wyss.website.discordbot.commands.playlist.PlaylistDeleteCommand;
import wyss.website.discordbot.commands.playlist.PlaylistPersister;
import wyss.website.discordbot.commands.playlist.PlaylistPlayCommand;
import wyss.website.discordbot.commands.playlist.PlaylistRemoveCommand;
import wyss.website.discordbot.commands.playlist.PlaylistsCommand;

public class DiscordListener {
  private static final Logger LOGGER = LoggerFactory.getLogger(DiscordListener.class);

  private AudioPlayerManager playerManager;
  private Map<Long, GuildMusicManager> musicManagers;

  private List<Command> commands;
  private HelpCommand helpCommand = new HelpCommand();
  private MusicPanelCommand musicPanelCommand = new MusicPanelCommand();

  private IDiscordClient client;

  private boolean lock = false;
  private List<String> admins = new ArrayList<>();

  private PlaylistPersister playlistPersister;

  @EventSubscriber
  public void onReady(ReadyEvent event) {
    client = event.getClient();
    musicManagers = new HashMap<>();

    try {
      playlistPersister = new PlaylistPersister(getPath());
      playlistPersister.loadPlaylists();
    } catch (URISyntaxException | IOException e) {
      e.printStackTrace();
    }

    playerManager = new DefaultAudioPlayerManager();
    AudioSourceManagers.registerRemoteSources(playerManager);
    AudioSourceManagers.registerLocalSource(playerManager);

    commands = new ArrayList<>();
    commands.add(helpCommand);
    commands.add(new HelloCommand());
    commands.add(new JoinCommand());
    commands.add(new LeaveCommand());
    commands.add(new MusicPlayCommand());
    commands.add(new MusicPlayNextCommand());
    commands.add(new MusicQueueCommand());
    commands.add(new MusicReplaceCommand());
    commands.add(new MusicRemoveCurrentSongCommand());
    commands.add(new MusicNextCommand());
    commands.add(new MusicPreviousCommand());
    commands.add(new MusicPauseCommand());
    commands.add(new MusicContinueCommand());
    commands.add(new MusicClearCommand());
    commands.add(new MusicSetVolumeCommand());
    commands.add(new MusicRepeatCommand());
    commands.add(new MusicRepeatSongCommand());
    commands.add(musicPanelCommand);
    commands.add(new MusicDurationCommand());
    commands.add(new PlaylistPlayCommand());
    commands.add(new PlaylistAddCommand());
    commands.add(new PlaylistRemoveCommand());
    commands.add(new PlaylistsCommand());
    commands.add(new PlaylistDeleteCommand());
    commands.add(new AnnounceCommand());
    commands.add(new FlipACoinCommand());
    commands.add(new ShutupCommand());
    commands.add(new ShutdownCommand());
    commands.add(new MessageCommand());
    commands.add(new AdminCommand());
    commands.add(new DeadminCommand());
    commands.add(new LockCommand());
    commands.add(new RebootCommand());

    event.getClient().changePresence(StatusType.ONLINE, ActivityType.PLAYING, helpCommand.getCommandPatternText());
  }

  public synchronized GuildMusicManager getGuildAudioPlayer(IGuild guild) {
    GuildMusicManager musicManager = musicManagers.computeIfAbsent(guild.getLongID(),
        id -> new GuildMusicManager(playerManager));
    guild.getAudioManager().setAudioProvider(musicManager.getAudioProvider());
    return musicManager;
  }

  @EventSubscriber
  public void onMessageReceived(MessageReceivedEvent event) {
    if (event.getMessage().getContent().startsWith(Command.COMMAND_PREFIX)) {
      if (!lock || isAdmin(event.getAuthor())) {
        try {
          RequestBuffer.request(() -> event.getMessage().delete()).get();
          boolean commandRecognized = false;
          for (Command command : commands) {
            boolean matches = command.executeIfmatches(event, this);
            commandRecognized = matches || commandRecognized;
          }
          if (!commandRecognized) {
            RequestBuffer.request(() -> event.getChannel().sendMessage("Command '" + event.getMessage().getContent()
                + "' not recognized. Type \"" + helpCommand.getCommandPatternText() + "\" for help."));
          }
        } catch (Exception e) {
          LOGGER.error("Stacktrace: ", e);
          RequestBuffer.request(() -> event.getChannel().sendMessage("An error occured while processing the command"));
          throw e;
        }
      } else {
        RequestBuffer
            .request(() -> event.getChannel().sendMessage("Lock mode is active. Only admins can use commands."));
      }
    }
  }

  @EventSubscriber
  public void onReactionEvent(ReactionEvent event) {
    IUser ourUser = event.getClient().getOurUser();
    IUser user = event.getUser();
    if (ourUser.equals(event.getMessage().getAuthor()) && !ourUser.equals(user)) {
      if (!lock || isAdmin(user)) {
        GuildMusicManager guildAudioPlayer = getGuildAudioPlayer(event.getGuild());
        TrackScheduler scheduler = guildAudioPlayer.scheduler;
        ReactionEmoji emoji = event.getReaction().getEmoji();
        LOGGER.debug("Reaction recived from {} ({})", user.getName(), user.getStringID());
        if (MusicPanel.ARROW_BACK.equals(emoji)) {
          scheduler.previousTrack();
          if (!isCurrentVoiceChannelNotEmpty(event.getGuild())) {
            scheduler.pausePlaying();
          }
        } else if (MusicPanel.PAUSE_PLAY.equals(emoji)) {
          if (isCurrentVoiceChannelNotEmpty(event.getGuild())) {
            scheduler.togglePause();
          }
        } else if (MusicPanel.ARROW_FORWARD.equals(emoji)) {
          scheduler.nextTrack();
          if (!isCurrentVoiceChannelNotEmpty(event.getGuild())) {
            scheduler.pausePlaying();
          }
        } else if (MusicPanel.VOLUME_DOWN.equals(emoji)) {
          guildAudioPlayer.volumeDown();
        } else if (MusicPanel.VOLUME_UP.equals(emoji)) {
          guildAudioPlayer.volumeUp();
        } else if (MusicPanel.REPEATE_EMOJI.equals(emoji)) {
          scheduler.toggleRepeatePlaylist();
        } else if (MusicPanel.REPEATE_ONE_EMOJI.equals(emoji)) {
          scheduler.toggleRepeateSong();
        } else if (MusicPanel.DURATION.equals(emoji)) {
          new DurationPanel(scheduler.getCurrentTrack()).send(event.getChannel());
        } else if (MusicPanel.REMOVE.equals(emoji)) {
          scheduler.removeCurrentSong();
        }
      } else {
        RequestBuffer
            .request(() -> event.getChannel().sendMessage("Lock mode is active. Only admins can use this functions."));
      }
    }
  }

  public boolean isAdmin(IUser user) {
    return getAdmins().contains(user.getStringID()) || "289059059829833728".equals(user.getStringID());
  }

  @EventSubscriber
  public void onVoiceChannelJoin(UserVoiceChannelJoinEvent event) {
    pauseIfVoiceChannelEmpty(event.getGuild());
  }

  @EventSubscriber
  public void onVoiceChannelLeave(UserVoiceChannelLeaveEvent event) {
    pauseIfVoiceChannelEmpty(event.getGuild());
  }

  @EventSubscriber
  public void onVoiceChannelMove(UserVoiceChannelMoveEvent event) {
    pauseIfVoiceChannelEmpty(event.getGuild());
  }

  private void pauseIfVoiceChannelEmpty(IGuild guild) {
    if (!isCurrentVoiceChannelNotEmpty(guild)) {
      getGuildAudioPlayer(guild).scheduler.pausePlaying();
    }
  }

  public boolean isCurrentVoiceChannelNotEmpty(IGuild guild) {
    IVoiceChannel currentChannel = client.getOurUser().getVoiceStateForGuild(guild).getChannel();
    if (currentChannel != null) {
      if (currentChannel.getConnectedUsers().size() == 1) {
        return false;
      } else if (currentChannel.getConnectedUsers().size() > 1) {
        return true;
      }
    }
    return false;
  }

  public List<Command> getCommands() {
    return commands;
  }

  public AudioPlayerManager getPlayerManager() {
    return playerManager;
  }

  public List<String> getAdmins() {
    return admins;
  }

  public void shutdown() {
    musicPanelCommand.deleteAll();
    client.logout();
  }

  public void lock() {
    lock = !lock;
  }

  public void admin(IUser user) {
    getAdmins().add(user.getStringID());
  }

  public void deadmin(IUser user) {
    getAdmins().remove(user.getStringID());
  }

  public boolean isLock() {
    return lock;
  }

  public PlaylistPersister getPlaylistPersister() {
    return playlistPersister;
  }

  private Path getPath() throws URISyntaxException {
    return getRootUrl();
  }

  private Path getRootUrl() throws URISyntaxException {
    return Paths.get(PlaylistPersister.class.getProtectionDomain().getCodeSource().getLocation().toURI()).getParent()
        .resolve("discordbot");
  }
}
