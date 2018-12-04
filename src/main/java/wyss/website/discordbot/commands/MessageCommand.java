package wyss.website.discordbot.commands;

import java.util.List;

import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.util.RequestBuffer;
import wyss.website.discordbot.DiscordListener;

public class MessageCommand extends Command {

  private static final String COMMAND_PATTERN = "message (.+)";
  private static final String COMMAND_PATTERN_DESCRIPTION = "message <text>";

  public MessageCommand() {
    super(COMMAND_PATTERN, COMMAND_PATTERN_DESCRIPTION);
    setAdminOnly(true);
  }

  @Override
  public void execute(MessageReceivedEvent event, DiscordListener discordListener, List<String> params) {
    RequestBuffer.request(() -> event.getChannel().sendMessage(params.get(0), false));
  }

  @Override
  public String getDescription() {
    return "Writes something";
  }
}
