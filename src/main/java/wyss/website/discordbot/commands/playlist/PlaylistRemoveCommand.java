package wyss.website.discordbot.commands.playlist;

import java.util.List;

import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.util.RequestBuffer;
import wyss.website.discordbot.DiscordListener;
import wyss.website.discordbot.commands.Command;

public class PlaylistRemoveCommand extends Command {

  private static final String COMMAND_PATTERN = "playlistRemove (.+) (.+)";
  private static final String COMMAND_PATTERN_DESCRIPTION = "playlistRemove <name> <url>";

  public PlaylistRemoveCommand() {
    super(COMMAND_PATTERN, COMMAND_PATTERN_DESCRIPTION);
  }

  @Override
  public void execute(MessageReceivedEvent event, DiscordListener discordListener, List<String> params) {
    String name = params.get(0);
    String url = params.get(1);
    Playlist playlist = discordListener.getPlaylistPersister().getPlaylists().get(name);
    if (playlist == null) {
      RequestBuffer.request(() -> event.getChannel().sendMessage("Playlist not found", false));
    } else {
      playlist.getSongs().removeIf(song -> song.trim().equals(url.trim()));
    }
  }

  @Override
  public String getDescription() {
    return "Removes the song from the playlist";
  }
}
