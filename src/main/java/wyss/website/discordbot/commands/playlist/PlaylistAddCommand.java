package wyss.website.discordbot.commands.playlist;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import wyss.website.discordbot.DiscordListener;
import wyss.website.discordbot.commands.Command;

public class PlaylistAddCommand extends Command {

  private static final String COMMAND_PATTERN = "playlistAdd (.+) (.+)";
  private static final String COMMAND_PATTERN_DESCRIPTION = "playlistAdd <name(no whitespace)> <url>";

  public PlaylistAddCommand() {
    super(COMMAND_PATTERN, COMMAND_PATTERN_DESCRIPTION);
  }

  @Override
  public void execute(MessageReceivedEvent event, DiscordListener discordListener, List<String> params) {
    String name = params.get(0);
    String url = params.get(1);
    Playlist playlist = discordListener.getPlaylistPersister().getPlaylists().getOrDefault(name, new Playlist(name));
    playlist.getSongs().add(url);
    try {
      discordListener.getPlaylistPersister().save(playlist);
    } catch (URISyntaxException | IOException e) {
      e.printStackTrace();
    }
  }

  @Override
  public String getDescription() {
    return "Creats and/or adds the song to the playlist (if the url points to a playlist, the whole playlist will be added)";
  }
}
