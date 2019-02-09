package wyss.website.discordbot.music;

import java.nio.ByteBuffer;
import java.time.Instant;

import com.sedmelluq.discord.lavaplayer.format.StandardAudioDataFormats;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.track.playback.MutableAudioFrame;

import discord4j.voice.AudioProvider;

class LavaplayerAudioProvider extends AudioProvider {

  private final AudioPlayer player;
  private final MutableAudioFrame frame = new MutableAudioFrame();

  LavaplayerAudioProvider(AudioPlayer player) {
    super(ByteBuffer.allocate(StandardAudioDataFormats.DISCORD_OPUS.maximumChunkSize()));
    this.player = player;
    this.frame.setBuffer(getBuffer());
  }

  Instant lastInstant = Instant.now();

  @Override
  public boolean provide() {
    boolean didProvide = player.provide(frame);
    if (didProvide) {
      getBuffer().flip();
    }
    return didProvide;
  }
}
