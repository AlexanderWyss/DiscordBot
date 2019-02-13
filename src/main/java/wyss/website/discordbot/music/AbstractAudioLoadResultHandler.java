package wyss.website.discordbot.music;

import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

public abstract class AbstractAudioLoadResultHandler {

  public void trackLoaded(AudioTrack track, String url) {
  }

  public void playlistLoaded(AudioPlaylist playlist, String url) {
  }

  public void noMatches(String url) {
  }

  public void loadFailed(FriendlyException exception, String url) {
  }

  public void loadSuccessful(String url) {

  }
}
