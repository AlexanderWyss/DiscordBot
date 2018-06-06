package wyss.website.discordbot;

import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.concurrent.LinkedBlockingDeque;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;

public class TrackScheduler extends AudioEventAdapter {
  private final AudioPlayer player;

  private Deque<AudioTrack> queue = new LinkedBlockingDeque<>();
  private Deque<AudioTrack> previousQueue = new LinkedBlockingDeque<>();

  private boolean repeatePlaylist = false;
  private boolean repeateSong = false;

  private AudioTrack currentTrack = null;

  public TrackScheduler(AudioPlayer player) {
    this.player = player;
  }

  public void queue(AudioTrack track) {
    queue.addLast(track);
    updateAll();
  }

  public void queue(AudioPlaylist playlist) {
    for (AudioTrack track : playlist.getTracks()) {
      queue.addLast(track);
    }
    updateAll();
  }

  public void queueNext(AudioTrack track) {
    queue.addFirst(track);
    updateAll();
  }

  public void queueNext(AudioPlaylist playlist) {
    List<AudioTrack> tracks = playlist.getTracks();
    for (int i = tracks.size() - 1; i >= 0; i--) {
      queue.addFirst(tracks.get(i));
    }
    updateAll();
  }

  public void nextTrack() {
    if (currentTrack != null) {
      previousQueue.addLast(currentTrack);
      currentTrack = null;
    }
    AudioTrack track;
    if ((track = queue.pollFirst()) != null) {
      play(track);
      currentTrack = track;
    } else {
      if (repeatePlaylist) {
        Deque<AudioTrack> queueTemp = queue;
        queue = previousQueue;
        previousQueue = queueTemp;
        nextTrack();
      }
    }
  }

  public void previousTrack() {
    if (currentTrack != null) {
      queue.addFirst(currentTrack);
      currentTrack = null;
    }
    AudioTrack track;
    if ((track = previousQueue.pollLast()) != null) {
      play(track);
      currentTrack = track;
    } else {
      nextTrack();
    }
  }

  private void play(AudioTrack track) {
    player.startTrack(track.makeClone(), false);
  }

  public void clear() {
    queue.clear();
    previousQueue.clear();
    currentTrack = null;
    player.stopTrack();
    updateAll();
  }

  @Override
  public void onTrackEnd(AudioPlayer player, AudioTrack track, AudioTrackEndReason endReason) {
    if (repeateSong && endReason.equals(AudioTrackEndReason.FINISHED)) {
      play(currentTrack);
    } else if (endReason.mayStartNext) {
      nextTrack();
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
    nextTrack();
  }

  // not current track form this instance must be playingtrack from player
  public AudioTrack getCurrentTrack() {
    return player.getPlayingTrack();
  }

  public int getNumberOfSongsInQueue() {
    return queue.size();
  }

  public int getNumberOfSongsPreviouslyInQueue() {
    return previousQueue.size();
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

  public void setPaused(boolean paused) {
    player.setPaused(paused);
    if (!paused && getCurrentTrack() == null) {
      previousTrack();
    }
  }

  public void setRepeatePlaylist(boolean repeatePlaylist) {
    this.repeatePlaylist = repeatePlaylist;
    updateAll();
  }

  public boolean isRepeatePlaylistSet() {
    return repeatePlaylist;
  }

  public void toggleRepeatePlaylist() {
    setRepeatePlaylist(!isRepeatePlaylistSet());
  }

  public void setRepeateSong(boolean repeateSong) {
    this.repeateSong = repeateSong;
    updateAll();
  }

  public boolean isRepeateSongSet() {
    return repeateSong;
  }

  public void toggleRepeateSong() {
    setRepeateSong(!isRepeateSongSet());
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
}
