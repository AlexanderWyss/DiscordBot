package wyss.website.discordbot.commands;

import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import wyss.website.discordbot.DiscordListener;

public interface Command {

  boolean matches(MessageReceivedEvent event, DiscordListener discordListener);

  void execute(MessageReceivedEvent event, DiscordListener discordListener);

  String getCommandPatternDescription();

  String getDescription();

  default boolean isSecret() {
    return false;
  }

}
