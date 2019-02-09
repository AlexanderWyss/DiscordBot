package wyss.website.discordbot.music;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

public class TrackScheduler extends AudioEventAdapter {

  private AudioPlayer player;

  public TrackScheduler(AudioPlayer player) {
    this.player = player;
    player.addListener(this);
  }

  public void play(AudioTrack track) {
    player.startTrack(track, false);
  }
}
