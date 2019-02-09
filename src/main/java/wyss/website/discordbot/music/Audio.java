package wyss.website.discordbot.music;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

public class Audio extends AudioTrackDelegator {
  public Audio(AudioTrack audioTrack) {
    super(audioTrack);
  }
}
