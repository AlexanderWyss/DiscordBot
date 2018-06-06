package wyss.website.discordbot.commands;

import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import wyss.website.discordbot.DiscordListener;

public class ShutdownCommand implements Command {

  private static final String COMMAND_TEXT = "Shutdown Bot";

  @Override
  public boolean matches(MessageReceivedEvent event, DiscordListener discordListener) {
    return event.getMessage().getFormattedContent().equalsIgnoreCase(COMMAND_TEXT);
  }

  @Override
  public void execute(MessageReceivedEvent event, DiscordListener discordListener) {
    event.getClient().logout();
  }

  @Override
  public String getCommandPatternDescription() {
    return COMMAND_TEXT;
  }

  @Override
  public String getDescription() {
    return "Shuts down the Bot (Not Rebootable from Discord)";
  }

}
