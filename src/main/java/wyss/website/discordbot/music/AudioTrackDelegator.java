package wyss.website.discordbot.music;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManager;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackInfo;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackState;
import com.sedmelluq.discord.lavaplayer.track.InternalAudioTrack;
import com.sedmelluq.discord.lavaplayer.track.TrackMarker;
import com.sedmelluq.discord.lavaplayer.track.playback.AudioFrame;
import com.sedmelluq.discord.lavaplayer.track.playback.AudioTrackExecutor;
import com.sedmelluq.discord.lavaplayer.track.playback.LocalAudioTrackExecutor;
import com.sedmelluq.discord.lavaplayer.track.playback.MutableAudioFrame;

public abstract class AudioTrackDelegator implements InternalAudioTrack {
  @Override
  public AudioTrackInfo getInfo() {
    return audioTrack.getInfo();
  }

  @Override
  public AudioFrame provide() {
    return audioTrack.provide();
  }

  @Override
  public String getIdentifier() {
    return audioTrack.getIdentifier();
  }

  @Override
  public AudioFrame provide(long timeout, TimeUnit unit) throws TimeoutException, InterruptedException {
    return audioTrack.provide(timeout, unit);
  }

  @Override
  public AudioTrackState getState() {
    return audioTrack.getState();
  }

  @Override
  public void stop() {
    audioTrack.stop();
  }

  @Override
  public void assignExecutor(AudioTrackExecutor executor, boolean applyPrimordialState) {
    audioTrack.assignExecutor(executor, applyPrimordialState);
  }

  @Override
  public boolean isSeekable() {
    return audioTrack.isSeekable();
  }

  @Override
  public long getPosition() {
    return audioTrack.getPosition();
  }

  @Override
  public void setPosition(long position) {
    audioTrack.setPosition(position);
  }

  @Override
  public AudioTrackExecutor getActiveExecutor() {
    return audioTrack.getActiveExecutor();
  }

  @Override
  public void process(LocalAudioTrackExecutor executor) throws Exception {
    audioTrack.process(executor);
  }

  @Override
  public void setMarker(TrackMarker marker) {
    audioTrack.setMarker(marker);
  }

  @Override
  public boolean provide(MutableAudioFrame targetFrame) {
    return audioTrack.provide(targetFrame);
  }

  @Override
  public long getDuration() {
    return audioTrack.getDuration();
  }

  @Override
  public AudioTrack makeClone() {
    return audioTrack.makeClone();
  }

  @Override
  public boolean provide(MutableAudioFrame targetFrame, long timeout, TimeUnit unit)
      throws TimeoutException, InterruptedException {
    return audioTrack.provide(targetFrame, timeout, unit);
  }

  @Override
  public AudioTrackExecutor createLocalExecutor(AudioPlayerManager playerManager) {
    return audioTrack.createLocalExecutor(playerManager);
  }

  @Override
  public AudioSourceManager getSourceManager() {
    return audioTrack.getSourceManager();
  }

  @Override
  public void setUserData(Object userData) {
    audioTrack.setUserData(userData);
  }

  @Override
  public Object getUserData() {
    return audioTrack.getUserData();
  }

  @Override
  public <T> T getUserData(Class<T> klass) {
    return audioTrack.getUserData(klass);
  }

  private final InternalAudioTrack audioTrack;

  public AudioTrackDelegator(AudioTrack audioTrack) {
    this.audioTrack = (InternalAudioTrack) audioTrack;
  }

}
