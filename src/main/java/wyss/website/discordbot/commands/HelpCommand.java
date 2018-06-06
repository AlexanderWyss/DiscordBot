package wyss.website.discordbot.commands;

import java.util.List;

import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.util.EmbedBuilder;
import sx.blah.discord.util.RequestBuffer;
import wyss.website.discordbot.DiscordListener;

public class HelpCommand extends Command {

  private static final String COMMAND_PATTERN = "Help";

  public HelpCommand() {
    super(COMMAND_PATTERN);
  }

  @Override
  public void execute(MessageReceivedEvent event, DiscordListener discordListener, List<String> params) {
    StringBuilder builder = new StringBuilder();
    List<Command> commands = discordListener.getCommands();
    for (Command command : commands) {
      if (!command.isSecret()) {
        builder.append(command.getCommandPatternText()).append(" - ").append(command.getDescription())
            .append(System.lineSeparator());
      }
    }
    builder.append(System.lineSeparator()).append("All commands are case insensitiv");
    RequestBuffer.request(() -> event.getChannel()
        .sendMessage(new EmbedBuilder().withTitle("Command - Description").appendDesc(builder.toString()).build()));
  }

  @Override
  public String getDescription() {
    return "Helps you :D";
  }
}
