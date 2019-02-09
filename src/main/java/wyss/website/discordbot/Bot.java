package wyss.website.discordbot;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.playback.AllocatingAudioFrameBuffer;

import discord4j.core.DiscordClient;
import discord4j.core.DiscordClientBuilder;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.voice.AudioReceiver;
import wyss.website.discordbot.music.GuildMusicManager;
import wyss.website.discordbot.music.TrackScheduler;

public class Bot {
  private static AudioPlayerManager playerManager;

  public static void main(String[] args) {
    playerManager = new DefaultAudioPlayerManager();
    playerManager.getConfiguration().setFrameBufferFactory(AllocatingAudioFrameBuffer::new);
    AudioSourceManagers.registerRemoteSources(playerManager);
    GuildMusicManager guildMusicManager = new GuildMusicManager(playerManager);

    DiscordClient client = new DiscordClientBuilder("NDUzNDg0MjUwNTMzMTk5ODcz.D0B8aw.Rd1VgvMYTikvEdEDinsVpaa2x5E")
        .build();
    client.getEventDispatcher().on(MessageCreateEvent.class)
        .filter(event -> event.getMessage().getContent().map(content -> content.startsWith("!play")).orElse(false))
        .subscribe(event -> play(event, guildMusicManager));

    client.login().block();
  }

  private static void play(MessageCreateEvent event, GuildMusicManager guildMusicManager) {
    String url = event.getMessage().getContent().get().split(" ")[1];
    playerManager.loadItem(url, new MyAudioLoadResultHandler(guildMusicManager.getScheduler()));
    event.getMember().get().getVoiceState()
        .subscribe(voiceState -> voiceState.getChannel().subscribe(channel -> channel.join(spec -> {
          spec.setProvider(guildMusicManager.getAudioProvider());
          spec.setReceiver(new LoggingAudioReceiver());
        }).subscribe(connection -> System.out.println("Connected " + connection))));
  }

  private static class MyAudioLoadResultHandler implements AudioLoadResultHandler {

    private final TrackScheduler scheduler;

    private MyAudioLoadResultHandler(TrackScheduler scheduler) {
      this.scheduler = scheduler;
    }

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
  }

  private static class LoggingAudioReceiver extends AudioReceiver {

    @Override
    public void receive(char sequence, int timestamp, int ssrc, byte[] audio) {
      System.out.println("packet from: " + ssrc);
    }
  }
}
