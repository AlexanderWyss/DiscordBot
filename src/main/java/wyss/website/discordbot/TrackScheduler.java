package wyss.website.discordbot;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;

public class TrackScheduler extends AudioEventAdapter {
  private final AudioPlayer player;

  private final List<AudioTrack> tracks = new LinkedList<>();
  private ListIterator<AudioTrack> queue = tracks.listIterator();

  private boolean repeatePlaylist = false;
  private boolean repeateSong = false;

  public TrackScheduler(AudioPlayer player) {
    this.player = player;
  }

  public void queue(AudioTrack track) {
    int index = queue.nextIndex();
    tracks.add(track);
    queue = tracks.listIterator(index);
    updateAll();
  }

  public void queue(AudioPlaylist playlist) {
    int index = queue.nextIndex();
    for (AudioTrack track : playlist.getTracks()) {
      tracks.add(track);
    }
    queue = tracks.listIterator(index);
    updateAll();
  }

  public void queueNext(AudioTrack track) {
    queue.add(track);
    queue.previous();
    updateAll();
  }

  public void queueNext(AudioPlaylist playlist) {
    int index = queue.nextIndex();
    for (AudioTrack track : playlist.getTracks()) {
      queue.add(track);
    }
    queue = tracks.listIterator(index);
    updateAll();
  }

  public void nextTrack() {
    if (queue.hasNext()) {
      play(queue.next());
    } else {
      if (repeatePlaylist) {
        queue = tracks.listIterator();
        nextTrack();
      }
    }
  }

  public void previousTrack() {
    if (queue.hasPrevious()) {
      play(queue.previous());
    } else {
      nextTrack();
    }
  }

  private void play(AudioTrack track) {
    player.startTrack(track.makeClone(), false);
  }

  public void clear() {
    tracks.clear();
    queue = tracks.listIterator();
  }

  @Override
  public void onTrackEnd(AudioPlayer player, AudioTrack track, AudioTrackEndReason endReason) {
    if (repeateSong && endReason.equals(AudioTrackEndReason.FINISHED)) {
      previousTrack();
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

  public AudioTrack getCurrentTrack() {
    return player.getPlayingTrack();
  }

  public int getNumberOfSongsInQueue() {
    return tracks.size() - queue.previousIndex();
  }

  public int getNumberOfSongsPreviouslyInQueue() {
    return queue.previousIndex();
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
