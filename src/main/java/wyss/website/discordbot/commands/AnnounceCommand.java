package wyss.website.discordbot.commands;

import java.util.List;

import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.util.RequestBuffer;
import wyss.website.discordbot.DiscordListener;

public class AnnounceCommand extends Command {

  private static final String COMMAND_PATTERN = "announce (.+)";
  private static final String COMMAND_PATTERN_DESCRIPTION = "Announce <text>";

  public AnnounceCommand() {
    super(COMMAND_PATTERN, COMMAND_PATTERN_DESCRIPTION);
  }

  @Override
  public void execute(MessageReceivedEvent event, DiscordListener discordListener, List<String> params) {
    RequestBuffer.request(() -> event.getChannel().sendMessage(params.get(0), true));
  }

  @Override
  public String getDescription() {
    return "Announces something";
  }
}
