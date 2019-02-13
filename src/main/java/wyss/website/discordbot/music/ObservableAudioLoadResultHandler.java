package wyss.website.discordbot.music;

import java.util.Optional;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

public class ObservableAudioLoadResultHandler implements AudioLoadResultHandler {
  private final Optional<AbstractAudioLoadResultHandler> resultHandler;
  private String url;

  public ObservableAudioLoadResultHandler(Optional<AbstractAudioLoadResultHandler> resultHandler, String url) {
    this.resultHandler = resultHandler;
    this.url = url;
  }

  @Override
  public void trackLoaded(AudioTrack track) {
    resultHandler.ifPresent(handler -> {
      handler.trackLoaded(track, url);
      handler.loadSuccessful(url);
    });
  }

  @Override
  public void playlistLoaded(AudioPlaylist playlist) {
    resultHandler.ifPresent(handler -> {
      handler.playlistLoaded(playlist, url);
      handler.loadSuccessful(url);
    });
  }

  @Override
  public void noMatches() {
    resultHandler.ifPresent(handler -> handler.noMatches(url));
  }

  @Override
  public void loadFailed(FriendlyException exception) {
    resultHandler.ifPresent(handler -> handler.loadFailed(exception, url));
  }
}