package wyss.website.discordbot.music;

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

  public void play(String url, AbstractAudioLoadResultHandler audioLoadResultHandler) {
    Optional<AbstractAudioLoadResultHandler> resultHandler = Optional.ofNullable(audioLoadResultHandler);
    manager.loadItem(url, new ObservableAudioLoadResultHandler(resultHandler) {
      @Override
      public void trackLoaded(AudioTrack track) {
        scheduler.playNow(new Audio(track));
        super.trackLoaded(track);
      }

      @Override
      public void playlistLoaded(AudioPlaylist playlist) {
        scheduler.playNow(
            playlist.getTracks().stream().map(audioTrack -> new Audio(audioTrack)).collect(Collectors.toList()));
        super.playlistLoaded(playlist);
      }
    });
  }

  public void play(String url) {
    play(url, null);
  }
}
