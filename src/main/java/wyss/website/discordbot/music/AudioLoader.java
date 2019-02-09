package wyss.website.discordbot.music;

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

  public void play(String url) {
    manager.loadItem(url, new AudioLoadResultHandler() {

      @Override
      public void trackLoaded(AudioTrack track) {
        scheduler.play(track);
      }

      @Override
      public void playlistLoaded(AudioPlaylist playlist) {
        scheduler.play(playlist.getTracks().get(0));
      }

      @Override
      public void noMatches() {
      }

      @Override
      public void loadFailed(FriendlyException exception) {
      }
    });
  }

}
