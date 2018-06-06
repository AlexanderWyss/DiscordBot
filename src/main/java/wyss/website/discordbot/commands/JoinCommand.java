package wyss.website.discordbot.commands;

import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.obj.IVoiceChannel;
import sx.blah.discord.handle.obj.IVoiceState;
import sx.blah.discord.util.RequestBuffer;
import wyss.website.discordbot.DiscordListener;

public class JoinCommand implements Command {

  private static final String COMMAND_TEXT = "Join me Bot";

  @Override
  public boolean matches(MessageReceivedEvent event, DiscordListener discordListener) {
    return event.getMessage().getFormattedContent().equalsIgnoreCase(COMMAND_TEXT);
  }

  @Override
  public void execute(MessageReceivedEvent event, DiscordListener discordListener) {
    IVoiceState voiceState = event.getAuthor().getVoiceStateForGuild(event.getGuild());
    IVoiceChannel channel;
    if (voiceState != null && (channel = voiceState.getChannel()) != null) {
      channel.join();
    } else {
      RequestBuffer.request(() -> event.getChannel().sendMessage("Could not join Channel."));
    }
  }

  @Override
  public String getCommandPatternDescription() {
    return COMMAND_TEXT;
  }

  @Override
  public String getDescription() {
    return "Joins your current Channel";
  }

}
