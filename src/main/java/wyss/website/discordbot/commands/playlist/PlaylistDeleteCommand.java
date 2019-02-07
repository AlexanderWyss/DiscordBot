package wyss.website.discordbot.commands.playlist;

import java.io.IOException;
import java.util.List;

import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.util.RequestBuffer;
import wyss.website.discordbot.DiscordListener;
import wyss.website.discordbot.commands.Command;

public class PlaylistDeleteCommand extends Command {

  private static final String COMMAND_PATTERN = "playlistDelete (.+)";
  private static final String COMMAND_PATTERN_DESCRIPTION = "playlistDelete <name>";

  public PlaylistDeleteCommand() {
    super(COMMAND_PATTERN, COMMAND_PATTERN_DESCRIPTION);
  }

  @Override
  public void execute(MessageReceivedEvent event, DiscordListener discordListener, List<String> params) {
    String name = params.get(0);
    PlaylistPersister playlistPersister = discordListener.getPlaylistPersister();
    if (playlistPersister.getPlaylists().remove(name) == null) {
      RequestBuffer.request(() -> event.getChannel().sendMessage("Playlist not found", false));
    } else {
      try {
        playlistPersister.save();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

  @Override
  public String getDescription() {
    return "Removes the song from the playlist";
  }
}
