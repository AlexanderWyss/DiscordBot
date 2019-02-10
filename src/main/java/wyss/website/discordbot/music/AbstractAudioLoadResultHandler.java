package wyss.website.discordbot.music;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

public abstract class AbstractAudioLoadResultHandler implements AudioLoadResultHandler {

  @Override
  public void trackLoaded(AudioTrack track) {
  }

  @Override
  public void playlistLoaded(AudioPlaylist playlist) {
  }

  @Override
  public void noMatches() {
  }

  @Override
  public void loadFailed(FriendlyException exception) {
  }

  public void loadSuccessful() {

  }
}
