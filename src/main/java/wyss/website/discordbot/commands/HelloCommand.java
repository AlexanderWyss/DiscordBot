package wyss.website.discordbot.commands;

import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.util.RequestBuffer;
import wyss.website.discordbot.DiscordListener;

public class HelloCommand implements Command {

  private static final String COMMAND_TEXT = "Hello Bot";

  @Override
  public boolean matches(MessageReceivedEvent event, DiscordListener discordListener) {
    return event.getMessage().getFormattedContent().equalsIgnoreCase(COMMAND_TEXT);
  }

  @Override
  public void execute(MessageReceivedEvent event, DiscordListener discordListener) {
    RequestBuffer.request(() -> event.getChannel().sendMessage("Hello " + event.getAuthor().getName()));
  }

  @Override
  public String getCommandPatternDescription() {
    return COMMAND_TEXT;
  }

  @Override
  public String getDescription() {
    return "Says Hello";
  }
}
