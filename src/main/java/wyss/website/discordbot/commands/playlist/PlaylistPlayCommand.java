package wyss.website.discordbot.commands.playlist;

import java.util.List;

import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.util.RequestBuffer;
import wyss.website.discordbot.DiscordListener;
import wyss.website.discordbot.GuildMusicManager;
import wyss.website.discordbot.commands.Command;
import wyss.website.discordbot.commands.DiscordAudioLoadResultHandler;
import wyss.website.discordbot.commands.MusicPlayCommand;

public class PlaylistPlayCommand extends Command {

  private static final String COMMAND_PATTERN = "playlist (.+)";
  private static final String COMMAND_PATTERN_DESCRIPTION = "playlist <name(Case sensitive)>";

  public PlaylistPlayCommand() {
    super(COMMAND_PATTERN, COMMAND_PATTERN_DESCRIPTION);
  }

  @Override
  public void execute(MessageReceivedEvent event, DiscordListener discordListener, List<String> params) {
    String name = params.get(0);
    IChannel channel = event.getChannel();
    GuildMusicManager musicManager = discordListener.getGuildAudioPlayer(channel.getGuild());
    Playlist playlist = discordListener.getPlaylistPersister().getPlaylists().get(name);
    if (playlist == null) {
      RequestBuffer.request(() -> event.getChannel().sendMessage("Playlist not found", false));
    } else {
      List<String> songs = playlist.getSongs();
      if (!songs.isEmpty()) {
        new MusicPlayCommand().play(event, discordListener, songs.get(0));
        for (int i = 1; i < songs.size(); i++) {
          String url = songs.get(i);
          discordListener.getPlayerManager().loadItemOrdered(musicManager, url,
              new DiscordAudioLoadResultHandler(channel, url) {

                @Override
                public void trackLoaded(AudioTrack track) {
                  musicManager.scheduler.queue(track);
                }

                @Override
                public void playlistLoaded(AudioPlaylist playlist) {
                  musicManager.scheduler.queue(playlist);
                }
              });
        }
      } else {
        RequestBuffer.request(() -> event.getChannel().sendMessage("Playlist is empty", false));
      }
    }
  }

  @Override
  public String getDescription() {
    return "Plays the Song or Playlist now (Loading a playlist may take a while)";
  }
}
