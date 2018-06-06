package wyss.website.discordbot;

import java.util.ArrayList;
import java.util.List;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;

/**
 * Holder for both the player and a track scheduler for one guild.
 */
public class GuildMusicManager {
  /**
   * Audio player for the guild.
   */
  public final AudioPlayer player;
  /**
   * Track scheduler for the player.
   */
  public final TrackScheduler scheduler;

  private int volumeSteps = 20;

  /**
   * Creates a player and a track scheduler.
   * 
   * @param manager
   *          Audio player manager to use for creating the player.
   */
  public GuildMusicManager(AudioPlayerManager manager) {
    player = manager.createPlayer();
    scheduler = new TrackScheduler(player);
    player.addListener(scheduler);
  }

  public void volumeUp() {
    setVolume(getVolume() + volumeSteps);
  }

  public void volumeDown() {
    setVolume(getVolume() - volumeSteps);
  }

  public void setVolume(int volume) {
    player.setVolume(volume);
    updateAll();
  }

  public int getVolume() {
    return player.getVolume();
  }

  /**
   * @return Wrapper around AudioPlayer to use it as an AudioSendHandler.
   */
  public AudioProvider getAudioProvider() {
    return new AudioProvider(player);
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
