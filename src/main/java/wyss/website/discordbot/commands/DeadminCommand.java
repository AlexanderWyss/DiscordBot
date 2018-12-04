package wyss.website.discordbot.commands;

import java.util.List;

import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.obj.IUser;
import wyss.website.discordbot.DiscordListener;

public class DeadminCommand extends Command {

  private static final String COMMAND_PATTERN = "deadmin (.+)";
  private static final String COMMAND_PATTERN_DESCRIPTION = "deadmin <user>";

  public DeadminCommand() {
    super(COMMAND_PATTERN, COMMAND_PATTERN_DESCRIPTION);
    setAdminOnly(true);
  }

  @Override
  public void execute(MessageReceivedEvent event, DiscordListener discordListener, List<String> params) {
    for (IUser user : event.getMessage().getMentions()) {
      discordListener.deadmin(user);
    }
  }

  @Override
  public String getDescription() {
    return "deadmins someone";
  }
}
