package wyss.website.discordbot.music;

import java.util.List;
import java.util.Optional;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;

import wyss.website.discordbot.music.playlist.Playlist;

public class AudioLoader {

  private AudioPlayerManager manager;
  private TrackScheduler scheduler;

  public AudioLoader(AudioPlayerManager manager, TrackScheduler scheduler) {
    this.manager = manager;
    this.scheduler = scheduler;
  }

  public void playNow(String url, AbstractAudioLoadResultHandler audioLoadResultHandler) {
    manager.loadItemOrdered(url, url,
        new PlayNowAudioLoader(Optional.ofNullable(audioLoadResultHandler), url, scheduler));
  }

  public void playNow(String url) {
    playNow(url, null);
  }

  public void playNext(String url, AbstractAudioLoadResultHandler audioLoadResultHandler) {
    manager.loadItemOrdered(url, url,
        new PlayNextAudioLoader(Optional.ofNullable(audioLoadResultHandler), url, scheduler));
  }

  public void playNext(String url) {
    playNext(url, null);
  }

  public void queue(String url, AbstractAudioLoadResultHandler audioLoadResultHandler) {
    manager.loadItemOrdered(url, url,
        new QueueAudioLoader(Optional.ofNullable(audioLoadResultHandler), url, scheduler));
  }

  public void queue(String url) {
    queue(url, null);
  }

  public void playNow(Playlist playlist, AbstractAudioLoadResultHandler audioLoadResultHandler) {
    List<String> songs = playlist.getSongs();
    if (!songs.isEmpty()) {
      playNow(songs.get(0), audioLoadResultHandler);
      for (int i = songs.size() - 1; i > 0; i--) {
        playNext(songs.get(i), audioLoadResultHandler);
      }
    }
  }

  public void playNow(Playlist playlist) {
    playNow(playlist, null);
  }

  public void playNext(Playlist playlist, AbstractAudioLoadResultHandler audioLoadResultHandler) {
    List<String> songs = playlist.getSongs();
    for (int i = songs.size() - 1; i >= 0; i--) {
      playNext(songs.get(i), audioLoadResultHandler);
    }
  }

  public void playNext(Playlist playlist) {
    playNext(playlist, null);
  }

  public void queue(Playlist playlist, AbstractAudioLoadResultHandler audioLoadResultHandler) {
    List<String> songs = playlist.getSongs();
    for (String string : songs) {
      queue(string, audioLoadResultHandler);
    }
  }

  public void queue(Playlist playlist) {
    queue(playlist, null);
  }
}
