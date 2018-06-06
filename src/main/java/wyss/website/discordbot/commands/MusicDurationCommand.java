package wyss.website.discordbot.commands;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.util.RequestBuffer;
import wyss.website.discordbot.DiscordListener;
import wyss.website.discordbot.DurationPanel;

public class MusicDurationCommand implements Command {

  private static final String COMMAND_TEXT = "MusicBot duration";

  @Override
  public boolean matches(MessageReceivedEvent event, DiscordListener discordListener) {
    return event.getMessage().getFormattedContent().equalsIgnoreCase(COMMAND_TEXT);
  }

  @Override
  public void execute(MessageReceivedEvent event, DiscordListener discordListener) {
    AudioTrack currentTrack = discordListener.getGuildAudioPlayer(event.getGuild()).scheduler.getCurrentTrack();
    IChannel channel = event.getChannel();
    if (currentTrack != null) {
      new DurationPanel(currentTrack).send(channel);
    } else {
      RequestBuffer.request(() -> channel.sendMessage("No song playing"));
    }
  }

  @Override
  public String getCommandPatternDescription() {
    return COMMAND_TEXT;
  }

  @Override
  public String getDescription() {
    return "Shows the current Duration of the Song";
  }

}
