package wyss.website.discordbot.commands;

import java.io.IOException;
import java.util.List;

import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.util.RequestBuffer;
import wyss.website.discordbot.DiscordListener;

public class RebootCommand extends Command {
  private static final String COMMAND_PATTERN = "reboot";

  public RebootCommand() {
    super(COMMAND_PATTERN);
  }

  @Override
  protected void execute(MessageReceivedEvent event, DiscordListener discordListener, List<String> params) {
    RequestBuffer.request(() -> event.getChannel().sendMessage("Rebooting..."));
    try {
      Runtime.getRuntime().exec("sudo reboot now");
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Override
  public String getDescription() {
    return "Reboots the bot, use this command to fix any errors";
  }
}
