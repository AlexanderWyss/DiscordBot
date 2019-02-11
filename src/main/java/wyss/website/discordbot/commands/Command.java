package wyss.website.discordbot.commands;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.entity.Message;
import discord4j.core.spec.MessageCreateSpec;
import reactor.core.publisher.Mono;
import wyss.website.discordbot.GuildManager;
import wyss.website.discordbot.commands.music.MusicPanelCommand;
import wyss.website.discordbot.commands.music.PlayCommand;

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
    return message.substring(identifier.length() + manager.getCommandPrefix().length()).trim();
  }

  protected Mono<Message> reply(MessageCreateEvent event, String message) {
    return event.getMessage().getChannel().flatMap(channel -> channel.createMessage(message));
  }

  protected Mono<Message> reply(MessageCreateEvent event, Consumer<MessageCreateSpec> message) {
    return event.getMessage().getChannel().flatMap(channel -> channel.createMessage(message));
  }

  @SuppressWarnings("serial")
  public static List<Command> list(GuildManager manager) {
    return new ArrayList<Command>() {
      {
        add(new HelpCommand(manager));
        add(new PlayCommand(manager));
        add(new MusicPanelCommand(manager));
      }
    };
  }

  public abstract void execute(MessageCreateEvent event);

  public abstract String getDescription();

  public String getParameterDescription() {
    return "";
  }
}
