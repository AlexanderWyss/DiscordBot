package wyss.website.discordbot.commands;

import java.util.List;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.util.RequestBuffer;
import wyss.website.discordbot.DiscordListener;
import wyss.website.discordbot.DurationPanel;

public class MusicDurationCommand extends Command {

  private static final String COMMAND_PATTERN = "duration";

  public MusicDurationCommand() {
    super(COMMAND_PATTERN);
  }

  @Override
  public void execute(MessageReceivedEvent event, DiscordListener discordListener, List<String> params) {
    AudioTrack currentTrack = discordListener.getGuildAudioPlayer(event.getGuild()).scheduler.getCurrentTrack();
    IChannel channel = event.getChannel();
    if (currentTrack != null) {
      new DurationPanel(currentTrack).send(channel);
    } else {
      RequestBuffer.request(() -> channel.sendMessage("No song playing"));
    }
  }

  @Override
  public String getDescription() {
    return "Shows the current Duration of the Song";
  }

}
