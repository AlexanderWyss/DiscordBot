package wyss.website.discordbot.music;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

public class AudioLoader {

  private AudioPlayerManager manager;
  private TrackScheduler scheduler;

  public AudioLoader(AudioPlayerManager manager, TrackScheduler scheduler) {
    this.manager = manager;
    this.scheduler = scheduler;
  }

  public void playNow(String url, AbstractAudioLoadResultHandler audioLoadResultHandler) {
    manager.loadItemOrdered(url, url,
        new ObservableAudioLoadResultHandler(Optional.ofNullable(audioLoadResultHandler)) {
          @Override
          public void trackLoaded(AudioTrack track) {
            scheduler.playNow(new Audio(track));
            super.trackLoaded(track);
          }

          @Override
          public void playlistLoaded(AudioPlaylist playlist) {
            scheduler.playNow(wrapAudio(playlist));
            super.playlistLoaded(playlist);
          }
        });
  }

  public void playNow(String url) {
    playNow(url, null);
  }

  public void playNext(String url, AbstractAudioLoadResultHandler audioLoadResultHandler) {
    manager.loadItemOrdered(url, url,
        new ObservableAudioLoadResultHandler(Optional.ofNullable(audioLoadResultHandler)) {
          @Override
          public void trackLoaded(AudioTrack track) {
            scheduler.playNext(new Audio(track));
            super.trackLoaded(track);
          }

          @Override
          public void playlistLoaded(AudioPlaylist playlist) {
            scheduler.playNext(wrapAudio(playlist));
            super.playlistLoaded(playlist);
          }
        });
  }

  public void playNext(String url) {
    playNext(url, null);
  }

  public void queue(String url, AbstractAudioLoadResultHandler audioLoadResultHandler) {
    manager.loadItemOrdered(url, url,
        new ObservableAudioLoadResultHandler(Optional.ofNullable(audioLoadResultHandler)) {
          @Override
          public void trackLoaded(AudioTrack track) {
            scheduler.queue(new Audio(track));
            super.trackLoaded(track);
          }

          @Override
          public void playlistLoaded(AudioPlaylist playlist) {
            scheduler.queue(wrapAudio(playlist));
            super.playlistLoaded(playlist);
          }
        });
  }

  public void queue(String url) {
    queue(url, null);
  }

  private List<Audio> wrapAudio(AudioPlaylist playlist) {
    return playlist.getTracks().stream().map(audioTrack -> new Audio(audioTrack)).collect(Collectors.toList());
  }
}
