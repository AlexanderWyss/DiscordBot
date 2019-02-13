package wyss.website.discordbot.music;

import java.util.Optional;

import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

public class PlayNowAudioLoader extends ObservableAudioLoadResultHandler {
  private TrackScheduler scheduler;

  public PlayNowAudioLoader(Optional<AbstractAudioLoadResultHandler> resultHandler, String url,
      TrackScheduler scheduler) {
    super(resultHandler, url);
    this.scheduler = scheduler;
  }

  @Override
  public void trackLoaded(AudioTrack track) {
    scheduler.playNow(new Audio(track));
    super.trackLoaded(track);
  }

  @Override
  public void playlistLoaded(AudioPlaylist playlist) {
    scheduler.playNow(playlist);
    super.playlistLoaded(playlist);
  }
}