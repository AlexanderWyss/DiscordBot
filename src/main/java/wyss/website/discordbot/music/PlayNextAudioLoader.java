package wyss.website.discordbot.music;

import java.util.Optional;

import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

public class PlayNextAudioLoader extends ObservableAudioLoadResultHandler {
  private TrackScheduler scheduler;

  public PlayNextAudioLoader(Optional<AbstractAudioLoadResultHandler> resultHandler, String url,
      TrackScheduler scheduler) {
    super(resultHandler, url);
    this.scheduler = scheduler;
  }

  @Override
  public void trackLoaded(AudioTrack track) {
    scheduler.playNext(new Audio(track));
    super.trackLoaded(track);
  }

  @Override
  public void playlistLoaded(AudioPlaylist playlist) {
    scheduler.playNext(playlist);
    super.playlistLoaded(playlist);
  }
}