package wyss.website.discordbot;

import org.apache.commons.lang3.time.DurationFormatUtils;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.util.EmbedBuilder;
import sx.blah.discord.util.RequestBuffer;

public class DurationPanel {

  private static final String DURATION_PATTERN = "mm:ss";
  private AudioTrack track;

  public DurationPanel(AudioTrack track) {
    this.track = track;
  }

  public void send(IChannel channel) {
    RequestBuffer.request(() -> channel.sendMessage(new EmbedBuilder().withTitle("Duration")
        .appendDesc(DurationFormatUtils.formatDuration(track.getPosition(), DURATION_PATTERN) + "/"
            + DurationFormatUtils.formatDuration(track.getDuration(), DURATION_PATTERN))
        .build()));
  }
}
