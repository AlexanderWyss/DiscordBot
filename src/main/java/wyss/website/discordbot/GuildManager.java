package wyss.website.discordbot;

import java.util.List;
import java.util.function.Predicate;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import discord4j.core.DiscordClient;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.entity.Guild;
import wyss.website.discordbot.commands.Command;
import wyss.website.discordbot.music.GuildMusicManager;

public class GuildManager {

  private static final Logger LOGGER = LoggerFactory.getLogger(GuildManager.class);

  private final String COMMAND_PREFIX = "!";
  private Guild guild;
  private DiscordClient client;
  private GuildMusicManager guildMusicManager;

  private List<Command> commands = Command.list(this);

  public GuildManager(Guild guild) {
    this.guild = guild;
    client = guild.getClient();
    guildMusicManager = GuildMusicManager.build(this);
    client.getEventDispatcher().on(MessageCreateEvent.class).filter(this::isGuild)
        .filter(event -> event.getMessage().getContent().map(message -> message.startsWith(COMMAND_PREFIX)).get())
        .subscribe(event -> executeCommand(event));
  }

  private void executeCommand(MessageCreateEvent event) {
    commands.stream().filter(commands(event)).forEach(command -> {
      command.execute(event);
      LOGGER.info("Executing command: {}", event.getMessage().getContent().get());
    });
  }

  private Predicate<? super Command> commands(MessageCreateEvent event) {
    return command -> Pattern.compile(COMMAND_PREFIX + command.getIdentifier() + "\\b", Pattern.CASE_INSENSITIVE)
        .matcher(event.getMessage().getContent().orElse("")).find();
  }

  private boolean isGuild(MessageCreateEvent event) {
    return event.getGuildId().map(id -> guild.getId().equals(id)).orElse(false);
  }

  public String getCommandPrefix() {
    return COMMAND_PREFIX;
  }

  public Guild getGuild() {
    return guild;
  }

  public DiscordClient getClient() {
    return client;
  }

  public GuildMusicManager getGuildMusicManager() {
    return guildMusicManager;
  }

  public List<Command> getCommands() {
    return commands;
  }
}
