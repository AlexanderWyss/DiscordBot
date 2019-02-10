package wyss.website.discordbot.music;

import java.util.Optional;
import java.util.stream.Collectors;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
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
    manager.loadItem(url, new AudioLoadResultHandler() {

      @Override
      public void trackLoaded(AudioTrack track) {
        scheduler.playNow(new Audio(track));
        resultHandler.ifPresent(handler -> {
          handler.trackLoaded(track);
          handler.loadSuccessful();
        });
      }

      @Override
      public void playlistLoaded(AudioPlaylist playlist) {
        scheduler.playNow(
            playlist.getTracks().stream().map(audioTrack -> new Audio(audioTrack)).collect(Collectors.toList()));
        resultHandler.ifPresent(handler -> {
          handler.playlistLoaded(playlist);
          handler.loadSuccessful();
        });
      }

      @Override
      public void noMatches() {
        resultHandler.ifPresent(AudioLoadResultHandler::noMatches);
      }

      @Override
      public void loadFailed(FriendlyException exception) {
        resultHandler.ifPresent(handler -> handler.loadFailed(exception));
      }
    });
  }

  public void play(String url) {
    play(url, null);
  }
}
