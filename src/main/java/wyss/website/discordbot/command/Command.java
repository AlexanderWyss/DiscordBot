package wyss.website.discordbot.command;

import java.util.function.Consumer;

import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.entity.Message;
import discord4j.core.spec.MessageCreateSpec;
import reactor.core.publisher.Mono;
import wyss.website.discordbot.GuildManager;

public abstract class Command {
  private GuildManager manager;
  private String identifier;

  public Command(GuildManager manager, String identifier) {
    this.manager = manager;
    this.identifier = identifier;
  }

  public GuildManager getManager() {
    return manager;
  }

  public String getIdentifier() {
    return identifier;
  }

  public String cutOffCommand(MessageCreateEvent event) {
    String message = event.getMessage().getContent().get();
    return message.substring(identifier.length() + manager.getCommandExecutor().getPrefix().length()).trim();
  }

  public static Mono<Message> reply(MessageCreateEvent event, String message) {
    return event.getMessage().getChannel().flatMap(channel -> channel.createMessage(message));
  }

  public static Mono<Message> reply(MessageCreateEvent event, Consumer<MessageCreateSpec> message) {
    return event.getMessage().getChannel().flatMap(channel -> channel.createMessage(message));
  }

  public static Mono<Message> reply(MessageCreateEvent event, String title, String text) {
    return reply(event, spec -> spec.setEmbed(embed -> embed.setTitle(title).setDescription(text)));
  }

  public abstract void execute(MessageCreateEvent event);

  public abstract Help getHelp();
}
