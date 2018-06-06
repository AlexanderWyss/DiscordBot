package wyss.website.discordbot.commands;

import java.util.List;

import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.util.EmbedBuilder;
import sx.blah.discord.util.RequestBuffer;
import wyss.website.discordbot.DiscordListener;

public class HelpCommand implements Command {

  private static final String COMMAND_TEXT = "Bot Help";

  @Override
  public boolean matches(MessageReceivedEvent event, DiscordListener discordListener) {
    return event.getMessage().getFormattedContent().equalsIgnoreCase(COMMAND_TEXT);
  }

  // TODO
  @Override
  public void execute(MessageReceivedEvent event, DiscordListener discordListener) {
    StringBuilder builder = new StringBuilder();
    List<Command> commands = discordListener.getCommands();
    for (Command command : commands) {
      if (!command.isSecret()) {
        builder.append(command.getCommandPatternDescription()).append(" - ").append(command.getDescription())
            .append(System.lineSeparator());
      }
    }
    RequestBuffer.request(() -> event.getChannel()
        .sendMessage(new EmbedBuilder().withTitle("Command - Description").appendDesc(builder.toString()).build()));
  }

  @Override
  public String getCommandPatternDescription() {
    return COMMAND_TEXT;
  }

  @Override
  public String getDescription() {
    return "Helps you :D";
  }
}
