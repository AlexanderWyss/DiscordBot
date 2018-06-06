package wyss.website.discordbot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;

import sx.blah.discord.api.events.EventSubscriber;
import sx.blah.discord.handle.impl.events.ReadyEvent;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.impl.events.guild.channel.message.reaction.ReactionEvent;
import sx.blah.discord.handle.impl.obj.ReactionEmoji;
import sx.blah.discord.handle.obj.ActivityType;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.handle.obj.StatusType;
import sx.blah.discord.util.RequestBuffer;
import wyss.website.discordbot.commands.AnnounceCommand;
import wyss.website.discordbot.commands.Command;
import wyss.website.discordbot.commands.HelloCommand;
import wyss.website.discordbot.commands.HelpCommand;
import wyss.website.discordbot.commands.JoinCommand;
import wyss.website.discordbot.commands.LeaveCommand;
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
import wyss.website.discordbot.commands.MusicRepeateCommand;
import wyss.website.discordbot.commands.MusicRepeateSongCommand;
import wyss.website.discordbot.commands.MusicReplaceCommand;
import wyss.website.discordbot.commands.MusicSetVolumeCommand;
import wyss.website.discordbot.commands.ShutdownCommand;

public class DiscordListener {
  private static final Logger LOGGER = LoggerFactory.getLogger(DiscordListener.class);

  private AudioPlayerManager playerManager;
  private Map<Long, GuildMusicManager> musicManagers;

  private List<Command> commands;
  private HelpCommand helpCommand = new HelpCommand();;

  @EventSubscriber
  public void onReady(ReadyEvent event) {
    musicManagers = new HashMap<>();

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
    commands.add(new MusicNextCommand());
    commands.add(new MusicPreviousCommand());
    commands.add(new MusicPauseCommand());
    commands.add(new MusicContinueCommand());
    commands.add(new MusicClearCommand());
    commands.add(new MusicSetVolumeCommand());
    commands.add(new MusicRepeateCommand());
    commands.add(new MusicRepeateSongCommand());
    commands.add(new MusicPanelCommand());
    commands.add(new MusicDurationCommand());
    commands.add(new AnnounceCommand());
    commands.add(new ShutdownCommand());

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
    boolean commandRecognized = false;
    for (Command command : commands) {
      boolean matches = command.executeIfmatches(event, this);
      commandRecognized = matches || commandRecognized;
    }
    if (!commandRecognized) {
      RequestBuffer.request(() -> event.getChannel()
          .sendMessage("Command not recognized. Type \"" + helpCommand.getCommandPatternText() + "\" for help."));
    }
  }

  @EventSubscriber
  public void onReactionEvent(ReactionEvent event) {
    IUser ourUser = event.getClient().getOurUser();
    IUser user = event.getUser();
    if (ourUser.equals(event.getMessage().getAuthor()) && !ourUser.equals(user)) {
      GuildMusicManager guildAudioPlayer = getGuildAudioPlayer(event.getGuild());
      TrackScheduler scheduler = guildAudioPlayer.scheduler;
      ReactionEmoji emoji = event.getReaction().getEmoji();
      LOGGER.debug("Reaction recived from {} ({})", user.getName(), user.getStringID());
      if (MusicPanel.ARROW_BACK.equals(emoji)) {
        scheduler.previousTrack();
      } else if (MusicPanel.PAUSE_PLAY.equals(emoji)) {
        scheduler.togglePause();
      } else if (MusicPanel.ARROW_FORWARD.equals(emoji)) {
        scheduler.nextTrack();
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
      }
    }
  }

  public List<Command> getCommands() {
    return commands;
  }

  public AudioPlayerManager getPlayerManager() {
    return playerManager;
  }

  public List<String> getAdmins() {
    List<String> admins = new ArrayList<>();
    admins.add("289059059829833728");
    return admins;
  }
}
