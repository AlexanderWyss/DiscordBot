package wyss.website.discordbot.commands;

import java.util.List;

import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.obj.IChannel;
import wyss.website.discordbot.DiscordListener;
import wyss.website.discordbot.GuildMusicManager;

public class MusicQueueCommand extends Command {

  private static final String COMMAND_PATTERN = "queue (.+)";
  private static final String COMMAND_PATTERN_DESCRIPTION = "queue <url>";

  public MusicQueueCommand() {
    super(COMMAND_PATTERN, COMMAND_PATTERN_DESCRIPTION);
  }

  @Override
  public void execute(MessageReceivedEvent event, DiscordListener discordListener, List<String> params) {
    String url = params.get(0);
    IChannel channel = event.getChannel();
    GuildMusicManager musicManager = discordListener.getGuildAudioPlayer(channel.getGuild());
    discordListener.getPlayerManager().loadItemOrdered(musicManager, url,
        new DiscordAudioLoadResultHandler(channel, url) {

          @Override
          public void trackLoaded(AudioTrack track) {
            musicManager.scheduler.queue(track);
          }

          @Override
          public void playlistLoaded(AudioPlaylist playlist) {
            musicManager.scheduler.queue(playlist);
          }
        });
  }

  @Override
  public String getDescription() {
    return "Adds the Song or Playlist to the Queue";
  }

}
