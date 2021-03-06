package wyss.website.discordbot.commands;

import java.util.List;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.obj.IChannel;
import wyss.website.discordbot.DiscordListener;
import wyss.website.discordbot.GuildMusicManager;

public class MusicPlayCommand extends Command {

  private static final String COMMAND_PATTERN = "play (.+)";
  private static final String COMMAND_PATTERN_DESCRIPTION = "play <url>";

  public MusicPlayCommand() {
    super(COMMAND_PATTERN, COMMAND_PATTERN_DESCRIPTION);
  }

  @Override
  public void execute(MessageReceivedEvent event, DiscordListener discordListener, List<String> params) {
    String url = params.get(0);
    play(event, discordListener, url);
  }

  public void play(MessageReceivedEvent event, DiscordListener discordListener, String url) {
    IChannel channel = event.getChannel();
    GuildMusicManager musicManager = discordListener.getGuildAudioPlayer(channel.getGuild());
    discordListener.getPlayerManager().loadItemOrdered(musicManager, url,
        new DiscordAudioLoadResultHandler(channel, url) {

          @Override
          public void trackLoaded(AudioTrack track) {
            musicManager.scheduler.queueNext(track);
            musicManager.scheduler.nextTrack();
          }

          @Override
          public void playlistLoaded(AudioPlaylist playlist) {
            musicManager.scheduler.queueNext(playlist);
            musicManager.scheduler.nextTrack();
          }
        });
    new JoinCommand().execute(event, discordListener, null);

    ScheduledThreadPoolExecutor scheduledThreadPoolExecutor = new ScheduledThreadPoolExecutor(1);
    scheduledThreadPoolExecutor.schedule(() -> {
      if (!discordListener.isCurrentVoiceChannelNotEmpty(event.getGuild())) {
        musicManager.scheduler.pausePlaying();
      }
    }, 30, TimeUnit.SECONDS);
  }

  @Override
  public String getDescription() {
    return "Plays the Song or Playlist now (Loading a playlist may take a while)";
  }
}
