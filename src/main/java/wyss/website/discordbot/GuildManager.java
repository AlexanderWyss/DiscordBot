package wyss.website.discordbot;

import java.lang.reflect.InvocationTargetException;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import discord4j.core.DiscordClient;
import discord4j.core.event.domain.Event;
import discord4j.core.object.entity.Guild;
import discord4j.core.object.util.Snowflake;
import reactor.core.publisher.Flux;
import wyss.website.discordbot.command.CommandExecutor;
import wyss.website.discordbot.music.GuildMusicManager;

public class GuildManager {

  private static final Logger LOGGER = LoggerFactory.getLogger(GuildManager.class);

  private Guild guild;
  private DiscordClient client;
  private GuildMusicManager guildMusicManager;

  private CommandExecutor commandExecutor;

  public GuildManager(Guild guild) {
    this.guild = guild;
    client = guild.getClient();
    guildMusicManager = GuildMusicManager.build(this);
    commandExecutor = new CommandExecutor(this, "!");
    commandExecutor.listen();
  }

  @SuppressWarnings("unchecked")
  private boolean isGuild(Event event) {
    try {
      return ((Optional<Snowflake>) event.getClass().getMethod("getGuildId").invoke(event))
          .map(id -> guild.getId().equals(id)).orElse(false);
    } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException
        | SecurityException e) {
      LOGGER.warn("getGuildId reflection Error", e);
      return true;
    }
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

  public CommandExecutor getCommandExecutor() {
    return commandExecutor;
  }

  public <T extends Event> Flux<T> on(Class<T> clazz) {
    return client.getEventDispatcher().on(clazz).filter(this::isGuild);
  }
}
