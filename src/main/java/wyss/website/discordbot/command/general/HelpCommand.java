package wyss.website.discordbot.command.general;

import java.util.Arrays;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.spec.MessageCreateSpec;
import wyss.website.discordbot.GuildManager;
import wyss.website.discordbot.command.Command;
import wyss.website.discordbot.command.CommandExecutor;
import wyss.website.discordbot.command.Commands;
import wyss.website.discordbot.command.Help;

public class HelpCommand extends Command {

  public HelpCommand(GuildManager manager) {
    super(manager, "help");
  }

  @Override
  public void execute(MessageCreateEvent event) {
    CommandExecutor commandExecutor = getManager().getCommandExecutor();
    reply(event, buildMessage(commandExecutor)).subscribe();
  }

  private Consumer<MessageCreateSpec> buildMessage(CommandExecutor commandExecutor) {
    return spec -> spec.setEmbed(embed -> {
      embed.setTitle("Command - Description");
      for (Commands commands : commandExecutor.getCommands()) {
        StringBuilder content = new StringBuilder();
        for (Command command : commands.getCommands()) {
          Help help = command.getHelp();
          content.append(commandExecutor.getPrefix()).append(command.getIdentifier()).append(" ")
              .append(getParams(help.getParameters())).append(" - ").append(help.getShortDescription())
              .append(System.lineSeparator());
        }
        embed.addField(commands.getName(), content.toString(), false);
      }
    });
  }

  private String getParams(String[] params) {
    if (params.length == 0) {
      return "";
    }
    return Arrays.stream(params).collect(Collectors.joining("> <", "<", ">"));
  }

  @Override
  public Help getHelp() {
    return new Help("Helps you :D", "");
  }
}
