package wyss.website.discordbot.commands;

import java.util.List;

import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.obj.IChannel;
import wyss.website.discordbot.DiscordListener;
import wyss.website.discordbot.GuildMusicManager;

public class MusicPlayNextCommand extends Command {

  private static final String COMMAND_PATTERN = "playNext (.+)";
  private static final String COMMAND_PATTERN_DESCRIPTION = "playNext <url>";

  public MusicPlayNextCommand() {
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
            musicManager.scheduler.queueNext(track);
          }

          @Override
          public void playlistLoaded(AudioPlaylist playlist) {
            musicManager.scheduler.queueNext(playlist);
          }
        });
  }

  @Override
  public String getDescription() {
    return "Plays the Song or Playlist after the current Song";
  }

}
