package wyss.website.discordbot.commands;

import java.util.List;

import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.util.RequestBuffer;
import wyss.website.discordbot.DiscordListener;

public class HelloCommand extends Command {
  private static final String COMMAND_PATTERN = "Hello";

  public HelloCommand() {
    super(COMMAND_PATTERN);
  }

  @Override
  protected void execute(MessageReceivedEvent event, DiscordListener discordListener, List<String> params) {
    RequestBuffer.request(() -> event.getChannel().sendMessage("Hello " + event.getAuthor().getName()));
  }

  @Override
  public String getDescription() {
    return "Says Hello";
  }
}
