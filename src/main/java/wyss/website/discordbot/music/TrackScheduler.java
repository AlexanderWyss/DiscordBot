package wyss.website.discordbot.music;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;

public class TrackScheduler extends AudioEventAdapter implements Observer {

  private static final int VOLUME_STEP = 10;

  private static final Logger LOGGER = LoggerFactory.getLogger(TrackScheduler.class);

  private AudioPlayer player;

  private ObservableList<Audio> audioTracks = new ObservableList<>();
  private int index = -1;

  private Repeat repeat = Repeat.NONE;

  private boolean shuffle = false;

  public TrackScheduler(AudioPlayer player) {
    this.player = player;
    player.addListener(this);
    audioTracks.addListener(this);
  }

  public void playNow(Audio track) {
    playNext(track);
    next();
  }

  public void playNow(List<Audio> playlist) {
    playNext(playlist);
    next();
  }

  public void playNow(AudioPlaylist playlist) {
    playNow(wrapAudio(playlist));
  }

  public void playNext(Audio track) {
    audioTracks.add(index + 1, track);
  }

  public void playNext(List<Audio> playlist) {
    audioTracks.addAll(index + 1, playlist);
  }

  public void playNext(AudioPlaylist playlist) {
    playNext(wrapAudio(playlist));
  }

  public void queue(Audio audio) {
    audioTracks.add(audio);
  }

  public void queue(List<Audio> playlist) {
    audioTracks.addAll(playlist);
  }

  public void queue(AudioPlaylist playlist) {
    queue(wrapAudio(playlist));
  }

  private List<Audio> wrapAudio(AudioPlaylist playlist) {
    return playlist.getTracks().stream().map(Audio::new).collect(Collectors.toList());
  }

  public void clear() {
    play(null);
    audioTracks.clear();
    index = -1;
  }

  public void next() {
    if (index < audioTracks.size() - 1) {
      if (shuffle) {
        index = new Random().nextInt(audioTracks.size());
      } else {
        index++;
      }
      play();
      resume();
    } else {
      if (repeat.equals(Repeat.LIST)) {
        index = -1;
        next();
      }
    }
  }

  private void play() {
    play(audioTracks.get(index));
  }

  public void previous() {
    if (index - 1 >= 0) {
      index -= 2;
    } else {
      if (repeat.equals(Repeat.LIST)) {
        index = audioTracks.size() - 2;
      } else {
        index--;
      }
    }
    next();
  }

  public boolean jumpToSong(int index) {
    if (index >= 0 && index < audioTracks.size()) {
      this.index = index;
      play();
      return true;
    }
    return false;
  }

  private void play(Audio track) {
    player.playTrack(track == null ? null : track.makeClone());
  }

  public void removeCurrentSong() {
    if (index >= 0) {
      audioTracks.remove(index);
      index--;
      next();
    }
  }

  public void resume() {
    setPaused(false);
  }

  public void pause() {
    setPaused(true);
  }

  public void setPaused(boolean paused) {
    player.setPaused(paused);
  }

  public void togglePause() {
    setPaused(!isPaused());
  }

  public boolean isPaused() {
    return player.isPaused();
  }

  public void setRepeat(Repeat repeat) {
    this.repeat = repeat;
    updateAll();
  }

  public Repeat getRepeat() {
    return repeat;
  }

  public void volumeUp() {
    setVolume(getVolume() + VOLUME_STEP);
  }

  public void volumeDown() {
    setVolume(getVolume() - VOLUME_STEP);
  }

  public void setVolume(int volume) {
    player.setVolume(volume * 10);
    updateAll();
  }

  public int getVolume() {
    return player.getVolume() / 10;
  }

  public Audio getCurrentTrack() {
    return (Audio) player.getPlayingTrack();
  }

  public int getAmountOfSongsPreviously() {
    return index;
  }

  public int getAmountOfSongsInQueue() {
    return audioTracks.size() - (index + 1);
  }

  public List<Audio> getAudioTracks() {
    return audioTracks;
  }

  public boolean isShuffel() {
    return shuffle;
  }

  public void setShuffel(boolean shuffel) {
    this.shuffle = shuffel;
    updateAll();
  }

  @Override
  public void onTrackEnd(AudioPlayer player, AudioTrack track, AudioTrackEndReason endReason) {
    if (endReason.mayStartNext) {
      if (repeat.equals(Repeat.SONG)) {
        index--;
      }
      next();
    }
  }

  @Override
  public void onTrackStart(AudioPlayer player, AudioTrack track) {
    updateAll();
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
  public void onTrackException(AudioPlayer player, AudioTrack track, FriendlyException exception) {
    LOGGER.warn("Exception in playback", exception);
  }

  @Override
  public void onTrackStuck(AudioPlayer player, AudioTrack track, long thresholdMs) {
    LOGGER.warn("Track stuck");
    next();
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

  @Override
  public void update() {
    updateAll();
  }
}
