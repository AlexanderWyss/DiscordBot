package wyss.website.discordbot.commands;

import java.util.List;

import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.util.RequestBuffer;
import wyss.website.discordbot.DiscordListener;

public class ShutupCommand extends Command {
  private static final String COMMAND_PATTERN = "shutup";

  public ShutupCommand() {
    super(COMMAND_PATTERN, true);
  }

  @Override
  protected void execute(MessageReceivedEvent event, DiscordListener discordListener, List<String> params) {
    RequestBuffer.request(() -> event.getChannel().sendMessage("You mom gay, " + event.getAuthor().getName() + "."));
  }

  @Override
  public String getDescription() {
    return "Well just shutup";
  }
}
