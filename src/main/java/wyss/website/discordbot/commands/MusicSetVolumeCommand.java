package wyss.website.discordbot.commands;

import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.util.RequestBuffer;
import wyss.website.discordbot.DiscordListener;

public class MusicSetVolumeCommand implements Command {

  private static final String COMMAND_TEXT = "MusicBot setVolume ";

  @Override
  public boolean matches(MessageReceivedEvent event, DiscordListener discordListener) {
    return event.getMessage().getFormattedContent().toUpperCase().startsWith(COMMAND_TEXT.toUpperCase());
  }

  @Override
  public void execute(MessageReceivedEvent event, DiscordListener discordListener) {
    String substring = event.getMessage().getFormattedContent().substring(COMMAND_TEXT.length());
    try {
      int volume = Integer.parseInt(substring);
      discordListener.getGuildAudioPlayer(event.getGuild()).setVolume(volume);
    } catch (NumberFormatException e) {
      RequestBuffer.request(() -> event.getChannel().sendMessage(substring + " is not a valid number"));
    }
  }

  @Override
  public String getCommandPatternDescription() {
    return COMMAND_TEXT + "<volume (0-150)>";
  }

  @Override
  public String getDescription() {
    return "Sets the Volume";
  }

}
