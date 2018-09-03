package wyss.website.discordbot.commands;

import java.util.List;
import java.util.Random;

import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.util.RequestBuffer;
import wyss.website.discordbot.DiscordListener;

public class FlipACoinCommand extends Command {

  private static final String COMMAND_PATTERN = "FlipACoin";

  public FlipACoinCommand() {
    super(COMMAND_PATTERN);
  }

  @Override
  public void execute(MessageReceivedEvent event, DiscordListener discordListener, List<String> params) {
    RequestBuffer.request(() -> event.getChannel().sendMessage(new Random().nextBoolean() ? "Face" : "Tails", false));
  }

  @Override
  public String getDescription() {
    return "Flips a coin (face or tails)";
  }
}
