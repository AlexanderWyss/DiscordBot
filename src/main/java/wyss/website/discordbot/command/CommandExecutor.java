package wyss.website.discordbot.command;

import java.util.List;
import java.util.function.Predicate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import discord4j.core.event.domain.message.MessageCreateEvent;
import wyss.website.discordbot.GuildManager;

public class CommandExecutor {
  private static final Logger LOGGER = LoggerFactory.getLogger(CommandExecutor.class);

  private GuildManager guildManager;

  private List<Command> commands;

  private final String prefix;

  public CommandExecutor(GuildManager guildManager, String prefix) {
    this.guildManager = guildManager;
    this.prefix = prefix;
    commands = Command.list(guildManager);
  }

  public void listen() {
    guildManager.on(MessageCreateEvent.class)
        .filter(event -> event.getMessage().getContent().map(message -> message.startsWith(prefix)).get())
        .subscribe(this::executeCommand);
  }

  private void executeCommand(MessageCreateEvent event) {
    commands.stream().filter(commands(event)).forEach(command -> {
      try {
        command.execute(event);
      } catch (Exception e) {
        LOGGER.error("Exception in command: {}", command.getIdentifier());
        LOGGER.error("Exception: ", e);
      }
      LOGGER.info("Executing command: {}", event.getMessage().getContent().get());
    });
  }

  private Predicate<? super Command> commands(MessageCreateEvent event) {
    return command -> Helper.matcherCaseInsensitive("^" + prefix + command.getIdentifier() + "\\b",
        event.getMessage().getContent().orElse("")).find();
  }

  public List<Command> getCommands() {
    return commands;
  }

  public String getPrefix() {
    return prefix;
  }
}
