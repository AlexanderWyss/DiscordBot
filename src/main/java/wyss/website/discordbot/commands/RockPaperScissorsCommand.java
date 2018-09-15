package wyss.website.discordbot.commands;

import java.util.List;

import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import wyss.website.discordbot.DiscordListener;

public class RockPaperScissorsCommand extends Command {
  private static final String COMMAND_PATTERN = "rps (user)"; // TODO regex
  private static final String COMMAND_PATTERN_DESCRIPTION = "rps <user>";

  public RockPaperScissorsCommand() {
    super(COMMAND_PATTERN, COMMAND_PATTERN_DESCRIPTION);
  }

  @Override
  public void execute(MessageReceivedEvent event, DiscordListener discordListener, List<String> params) {
    String userName = params.get(0);
  }

  @Override
  public String getDescription() {
    return "Rock Paper Scissors";
  }
}
