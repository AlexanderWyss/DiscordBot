package wyss.website.discordbot.commands;

import java.util.stream.Collectors;

import discord4j.core.event.domain.message.MessageCreateEvent;
import wyss.website.discordbot.GuildManager;

public class HelpCommand extends Command {

  public HelpCommand(GuildManager manager) {
    super(manager, "help");
  }

  @Override
  public void execute(MessageCreateEvent event) {
    String description = getManager().getCommands().stream()
        .map(command -> getManager().getCommandPrefix() + command.getIdentifier() + " "
            + command.getParameterDescription() + " - " + command.getDescription())
        .collect(Collectors.joining(System.lineSeparator()));
    reply(event,
        spec -> spec.setEmbed(specEmbed -> specEmbed.setTitle("Command - Description").setDescription(description)))
            .subscribe();
  }

  @Override
  public String getDescription() {
    return "Helps you :D";
  }
}
