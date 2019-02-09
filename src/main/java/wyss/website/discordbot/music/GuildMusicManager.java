package wyss.website.discordbot.music;

import java.util.Optional;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.track.playback.AllocatingAudioFrameBuffer;

import discord4j.core.object.VoiceState;
import discord4j.core.object.entity.Member;
import discord4j.core.object.entity.VoiceChannel;
import discord4j.voice.VoiceConnection;
import reactor.core.publisher.Mono;

public class GuildMusicManager {
  private final TrackScheduler scheduler;

  private final LavaplayerAudioProvider audioProvider;

  private AudioLoader audioLoader;

  public GuildMusicManager(AudioPlayerManager manager) {
    AudioPlayer player = manager.createPlayer();
    scheduler = new TrackScheduler(player);
    audioProvider = new LavaplayerAudioProvider(player);
    audioLoader = new AudioLoader(manager, scheduler);
  }

  public Mono<VoiceConnection> join(VoiceChannel channel) {
    return channel.join(spec -> {
      spec.setProvider(audioProvider);
    });
  }

  public Mono<VoiceConnection> join(Member member) {
    return member.getVoiceState().flatMap(VoiceState::getChannel).flatMap(this::join);
  }

  public TrackScheduler getScheduler() {
    return scheduler;
  }

  public LavaplayerAudioProvider getAudioProvider() {
    return audioProvider;
  }

  public AudioLoader getAudioLoader() {
    return audioLoader;
  }

  public static GuildMusicManager build() {
    return new GuildMusicManagerBuilder().build();
  }

  private static class GuildMusicManagerBuilder {
    private static Optional<AudioPlayerManager> playerManager = Optional.empty();

    private GuildMusicManager build() {
      return new GuildMusicManager(playerManager.orElse(createAudioPlayerManager()));
    }

    private AudioPlayerManager createAudioPlayerManager() {
      playerManager = Optional.of(new DefaultAudioPlayerManager());
      playerManager.get().getConfiguration().setFrameBufferFactory(AllocatingAudioFrameBuffer::new);
      AudioSourceManagers.registerRemoteSources(playerManager.get());
      return playerManager.get();
    }
  }
}
