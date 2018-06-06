package wyss.website.discordbot.commands;

import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.util.RequestBuffer;
import wyss.website.discordbot.DiscordListener;

public class AnnounceCommand implements Command {

  private static final String COMMAND_TEXT = "Bot Announce ";

  @Override
  public boolean matches(MessageReceivedEvent event, DiscordListener discordListener) {
    return event.getMessage().getFormattedContent().toUpperCase().startsWith(COMMAND_TEXT.toUpperCase());
  }

  @Override
  public void execute(MessageReceivedEvent event, DiscordListener discordListener) {
    RequestBuffer.request(() -> event.getChannel()
        .sendMessage(event.getMessage().getFormattedContent().substring(COMMAND_TEXT.length()), true));
  }

  @Override
  public String getCommandPatternDescription() {
    return COMMAND_TEXT + "<text>";
  }

  @Override
  public String getDescription() {
    return "Announces something";
  }

}
