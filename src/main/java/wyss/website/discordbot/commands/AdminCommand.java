package wyss.website.discordbot.commands;

import java.util.List;

import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.obj.IUser;
import wyss.website.discordbot.DiscordListener;

public class AdminCommand extends Command {

  private static final String COMMAND_PATTERN = "admin (.+)";
  private static final String COMMAND_PATTERN_DESCRIPTION = "admin <user>";

  public AdminCommand() {
    super(COMMAND_PATTERN, COMMAND_PATTERN_DESCRIPTION);
    setAdminOnly(true);
  }

  @Override
  public void execute(MessageReceivedEvent event, DiscordListener discordListener, List<String> params) {
    for (IUser user : event.getMessage().getMentions()) {
      discordListener.admin(user);
    }
  }

  @Override
  public String getDescription() {
    return "admins someone";
  }
}
