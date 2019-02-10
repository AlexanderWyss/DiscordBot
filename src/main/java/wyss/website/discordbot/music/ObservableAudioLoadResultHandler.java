package wyss.website.discordbot.music;

import java.util.Optional;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

public class ObservableAudioLoadResultHandler implements AudioLoadResultHandler {
  private final Optional<AbstractAudioLoadResultHandler> resultHandler;

  public ObservableAudioLoadResultHandler(Optional<AbstractAudioLoadResultHandler> resultHandler) {
    this.resultHandler = resultHandler;
  }

  @Override
  public void trackLoaded(AudioTrack track) {
    resultHandler.ifPresent(handler -> {
      handler.trackLoaded(track);
      handler.loadSuccessful();
    });
  }

  @Override
  public void playlistLoaded(AudioPlaylist playlist) {
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
}