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
import wyss.website.discordbot.GuildManager;

public class GuildMusicManager {
  private final TrackScheduler scheduler;

  private AudioLoader audioLoader;
  private GuildManager guildManager;
  private AudioPlayer player;

  private Optional<VoiceConnection> voiceConnection = Optional.empty();

  public GuildMusicManager(AudioPlayerManager manager, GuildManager guildManager) {
    this.guildManager = guildManager;
    player = manager.createPlayer();
    scheduler = new TrackScheduler(player);
    audioLoader = new AudioLoader(manager, scheduler);
  }

  public void join(VoiceChannel channel) {
    channel.join(spec -> spec.setProvider(new LavaplayerAudioProvider(player)))
        .subscribe(connection -> voiceConnection = Optional.of(connection));
  }

  public void join(Member member) {
    member.getVoiceState().flatMap(VoiceState::getChannel).filterWhen(
        channel -> channel.getGuild().map(channelGuild -> guildManager.getGuild().getId().equals(channelGuild.getId())))
        .subscribe(this::join);
  }

  public void leave() {
    voiceConnection.ifPresent(connection -> {
      connection.disconnect();
      voiceConnection = Optional.empty();
    });
  }

  public TrackScheduler getScheduler() {
    return scheduler;
  }

  public AudioLoader getAudioLoader() {
    return audioLoader;
  }

  public AudioPlayer getPlayer() {
    return player;
  }

  public static GuildMusicManager build(GuildManager manager) {
    return new GuildMusicManagerBuilder().build(manager);
  }

  private static class GuildMusicManagerBuilder {
    private static Optional<AudioPlayerManager> playerManager = Optional.empty();

    private GuildMusicManager build(GuildManager manager) {
      return new GuildMusicManager(playerManager.orElse(createAudioPlayerManager()), manager);
    }

    private static AudioPlayerManager createAudioPlayerManager() {
      playerManager = Optional.of(new DefaultAudioPlayerManager());
      playerManager.get().getConfiguration().setFrameBufferFactory(AllocatingAudioFrameBuffer::new);
      AudioSourceManagers.registerRemoteSources(playerManager.get());
      return playerManager.get();
    }
  }
}
