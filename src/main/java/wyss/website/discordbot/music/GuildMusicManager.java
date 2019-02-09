package wyss.website.discordbot.music;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;

import discord4j.voice.AudioProvider;

public class GuildMusicManager {
  private final AudioPlayer player;

  private final TrackScheduler scheduler;

  private final LavaplayerAudioProvider lavaplayer;

  public GuildMusicManager(AudioPlayerManager manager) {
    player = manager.createPlayer();
    scheduler = new TrackScheduler(player);
    player.addListener(scheduler);
    lavaplayer = new LavaplayerAudioProvider(player);
  }

  public AudioProvider getAudioProvider() {
    return lavaplayer;
  }

  public AudioPlayer getPlayer() {
    return player;
  }

  public TrackScheduler getScheduler() {
    return scheduler;
  }
}
