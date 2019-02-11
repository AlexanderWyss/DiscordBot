package wyss.website.discordbot.command.commands;

import java.util.Arrays;
import java.util.stream.Collectors;

import discord4j.core.event.domain.message.MessageCreateEvent;
import wyss.website.discordbot.GuildManager;
import wyss.website.discordbot.command.Command;
import wyss.website.discordbot.command.CommandExecutor;
import wyss.website.discordbot.command.Help;

public class HelpCommand extends Command {

  public HelpCommand(GuildManager manager) {
    super(manager, "help");
  }

  @Override
  public void execute(MessageCreateEvent event) {
    CommandExecutor commandExecutor = getManager().getCommandExecutor();
    String description = commandExecutor.getCommands().stream().map(command -> {
      Help help = command.getHelp();
      return commandExecutor.getPrefix() + command.getIdentifier() + " "
          + Arrays.stream(help.getParameters()).collect(Collectors.joining("> <", "<", ">")) + " - "
          + help.getShortDescription();
    }).collect(Collectors.joining(System.lineSeparator()));
    reply(event,
        spec -> spec.setEmbed(specEmbed -> specEmbed.setTitle("Command - Description").setDescription(description)))
            .subscribe();
  }

  @Override
  public Help getHelp() {
    return new Help("Helps you :D", "");
  }
}
