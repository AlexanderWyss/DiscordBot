package wyss.website.discordbot.commands.playlist;

import java.util.Enumeration;
import java.util.List;

import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.util.EmbedBuilder;
import sx.blah.discord.util.RequestBuffer;
import wyss.website.discordbot.DiscordListener;
import wyss.website.discordbot.commands.Command;

public class PlaylistsCommand extends Command {

  private static final String COMMAND_PATTERN = "playlists";

  public PlaylistsCommand() {
    super(COMMAND_PATTERN);
  }

  @Override
  public void execute(MessageReceivedEvent event, DiscordListener discordListener, List<String> params) {
    EmbedBuilder message = new EmbedBuilder().withTitle("Playlists");
    Enumeration<String> playlists = discordListener.getPlaylistPersister().getPlaylists().keys();
    while (playlists.hasMoreElements()) {
      String playlist = playlists.nextElement() + "\n";
      if ((message.getTotalVisibleCharacters() + playlist.length()) > EmbedBuilder.MAX_CHAR_LIMIT) {
        send(event, message);
        message = new EmbedBuilder();
      }
      message.appendDesc(playlist);
    }
    send(event, message);
  }

  private void send(MessageReceivedEvent event, EmbedBuilder message) {
    RequestBuffer.request(() -> event.getChannel().sendMessage(message.build()));
  }

  @Override
  public String getDescription() {
    return "Lists all available playlists";
  }
}
