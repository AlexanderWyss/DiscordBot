package wyss.website.discordbot.commands;

import java.util.List;

import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import wyss.website.discordbot.DiscordListener;

public class ShutdownCommand extends Command {

  private static final String COMMAND_TEXT = "shutdown";

  public ShutdownCommand() {
    super(COMMAND_TEXT, true);
    setAdminOnly(true);
  }

  @Override
  public void execute(MessageReceivedEvent event, DiscordListener discordListener, List<String> params) {
    discordListener.shutdown();
  }

  @Override
  public String getDescription() {
    return "Shuts down the Bot (Not Rebootable from Discord)";
  }

}
