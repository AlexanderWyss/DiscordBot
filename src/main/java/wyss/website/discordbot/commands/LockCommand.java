package wyss.website.discordbot.commands;

import java.util.List;

import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.util.RequestBuffer;
import wyss.website.discordbot.DiscordListener;

public class LockCommand extends Command {

  private static final String COMMAND_TEXT = "lock";

  public LockCommand() {
    super(COMMAND_TEXT, true);
    setAdminOnly(true);
  }

  @Override
  public void execute(MessageReceivedEvent event, DiscordListener discordListener, List<String> params) {
    discordListener.lock();
    RequestBuffer.request(() -> event.getChannel()
        .sendMessage(discordListener.isLock() ? "Lock mode activated" : "Lock mode deactivated"));
  }

  @Override
  public String getDescription() {
    return "Lock";
  }

}
