package wyss.website.discordbot;

import java.util.ArrayList;
import java.util.List;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;

/**
 * This class schedules tracks for the audio player. It contains the queue of
 * tracks.
 */
// TODO rewrite
public class TrackScheduler extends AudioEventAdapter {
  private final AudioPlayer player;
  private final List<AudioTrack> queue;
  private int nextIndex = 0;
  private boolean repeatePlaylist = false;
  private boolean repeateSong = false;

  public TrackScheduler(AudioPlayer player) {
    this.player = player;
    this.queue = new ArrayList<>();
  }

  public void queue(AudioTrack track) {
    queue.add(track);
    updateAll();
  }

  public void queue(AudioPlaylist playlist) {
    for (AudioTrack track : playlist.getTracks()) {
      queue.add(track);
    }
    updateAll();
  }

  public void queueNext(AudioTrack track) {
    queue.add(nextIndex, track);
    updateAll();
  }

  public void queueNext(AudioPlaylist playlist) {
    List<AudioTrack> tracks = playlist.getTracks();
    for (int i = 0; i < tracks.size(); i++) {
      queue.add(nextIndex + i, tracks.get(i));
    }
    updateAll();
  }

  public void forward() {
    if (nextIndex < queue.size()) {
      play(nextIndex, false);
      continuePlaying();
      nextIndex++;
    } else {
      if (repeatePlaylist) {
        nextIndex = 0;
        forward();
      } else {
        nextIndex = queue.size();
      }
    }
  }

  public void previous() {
    nextIndex -= 2;
    if (nextIndex >= 0) {
      play(nextIndex, false);
      continuePlaying();
      nextIndex++;
    } else {
      nextIndex = 0;
      forward();
    }
  }

  public void togglePause() {
    setPaused(!player.isPaused());
  }

  public void pausePlaying() {
    setPaused(true);
  }

  public void continuePlaying() {
    setPaused(false);
  }

  public boolean isPaused() {
    return player.isPaused();
  }

  public void setPaused(boolean value) {
    player.setPaused(value);
    if (getCurrentTrack() == null) {
      nextIndex--;
      forward();
    }
  }

  // TODO remove
  public void play(int index, boolean notForce) {
    AudioTrack audioTrack = queue.get(index);
    player.startTrack(audioTrack.makeClone(), notForce);
    if (queue.size() > 1000) {
      while (index > 0) {
        queue.remove(0);
        index--;
      }
    }
  }

  public void clear() {
    queue.clear();
    nextIndex = 0;
  }

  @Override
  public void onTrackEnd(AudioPlayer player, AudioTrack track, AudioTrackEndReason endReason) {
    if (repeateSong && endReason.equals(AudioTrackEndReason.FINISHED)) {
      nextIndex--;
    }
    if (endReason.mayStartNext) {
      forward();
    }
  }

  @Override
  public void onPlayerPause(AudioPlayer player) {
    updateAll();
  }

  @Override
  public void onPlayerResume(AudioPlayer player) {
    updateAll();
  }

  @Override
  public void onTrackStart(AudioPlayer player, AudioTrack track) {
    updateAll();
  }

  @Override
  public void onTrackStuck(AudioPlayer player, AudioTrack track, long thresholdMs) {
    forward();
  }

  private List<Observer> observers = new ArrayList<>();

  public void updateAll() {
    for (Observer observer : observers) {
      observer.update();
    }
  }

  public void addListener(Observer observer) {
    observers.add(observer);
  }

  public void removeListener(Observer observer) {
    observers.remove(observer);
  }

  public AudioTrack getCurrentTrack() {
    return player.getPlayingTrack();
  }

  public int getSongsNumberInQueue() {
    return queue.size() - nextIndex;
  }

  public int getNumberOfSongsPreviouslyInQueue() {
    return nextIndex - 1;
  }

  public void repeatePlaylist(boolean repeatePlaylist) {
    this.repeatePlaylist = repeatePlaylist;
    updateAll();
  }

  public void repeateSong(boolean repeateSong) {
    this.repeateSong = repeateSong;
    updateAll();
  }

  public boolean isRepeatePlaylist() {
    return repeatePlaylist;
  }

  public boolean isRepeateSong() {
    return repeateSong;
  }

  public void toggleRepeatePlaylist() {
    repeatePlaylist(!isRepeatePlaylist());
  }

  public void toggleRepeateSong() {
    repeateSong(!isRepeateSong());
  }
}
