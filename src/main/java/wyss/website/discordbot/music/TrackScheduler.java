package wyss.website.discordbot.music;

import java.util.Collections;
import java.util.List;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;

public class TrackScheduler extends AudioEventAdapter {

  private AudioPlayer player;

  private List<Audio> audioTracks = new ObservableList<>();
  private int index = -1;

  public TrackScheduler(AudioPlayer player) {
    this.player = player;
    player.addListener(this);
  }

  public void playNow(Audio track) {
    addNext(track);
    next();
  }

  public void playNow(List<Audio> playlist) {
    Collections.reverse(playlist);
    playlist.forEach(this::addNext);
    next();
  }

  public void resume() {
    player.setPaused(false);
  }

  public void pause() {
    player.setPaused(true);
  }

  public void setPaused(boolean paused) {
    player.setPaused(paused);
  }

  public void togglePause() {
    player.setPaused(!player.isPaused());
  }

  public void next() {
    player.playTrack(audioTracks.get(++index));
    resume();
  }

  public void addNext(Audio track) {
    audioTracks.add(index + 1, track);
  }

  @Override
  public void onTrackEnd(AudioPlayer player, AudioTrack track, AudioTrackEndReason endReason) {
    next();
  }
}
