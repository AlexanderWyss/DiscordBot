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

  private Repeat repeat = Repeat.NONE;

  public TrackScheduler(AudioPlayer player) {
    this.player = player;
    player.addListener(this);
  }

  public void playNow(Audio track) {
    playNext(track);
    next();
  }

  public void playNow(List<Audio> playlist) {
    playNext(playlist);
    next();
  }

  public void playNext(Audio track) {
    audioTracks.add(index + 1, track);
  }

  public void playNext(List<Audio> playlist) {
    Collections.reverse(playlist);
    playlist.forEach(this::playNext);
  }

  public void queue(Audio audio) {
    audioTracks.add(audio);
  }

  public void queue(List<Audio> playlist) {
    playlist.forEach(this::queue);
  }

  public void clear() {
    player.playTrack(null);
    audioTracks.clear();
    index = -1;
  }

  public void next() {
    if (repeat.equals(Repeat.SONG)) {
      index--;
    }
    if (index < audioTracks.size() - 1) {
      player.playTrack(audioTracks.get(++index));
      resume();
    } else {
      if (repeat.equals(Repeat.LIST)) {
        index = -1;
        next();
      }
    }
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

  public void setRepeat(Repeat repeat) {
    this.repeat = repeat;
  }

  public Repeat getRepeat() {
    return repeat;
  }

  @Override
  public void onTrackEnd(AudioPlayer player, AudioTrack track, AudioTrackEndReason endReason) {
    if (endReason.mayStartNext) {
      next();
    }
  }
}
