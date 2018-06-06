package wyss.website.discordbot.commands;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.util.RequestBuffer;

public abstract class DiscordAudioLoadResultHandler implements AudioLoadResultHandler {
  private IChannel channel;
  private String url;

  public DiscordAudioLoadResultHandler(IChannel channel, String url) {
    this.channel = channel;
    this.url = url;
  }

  @Override
  public abstract void trackLoaded(AudioTrack track);

  @Override
  public abstract void playlistLoaded(AudioPlaylist playlist);

  @Override
  public void noMatches() {
    RequestBuffer.request(() -> channel.sendMessage("Nothing foud by " + url));
  }

  @Override
  public void loadFailed(FriendlyException exception) {
    RequestBuffer.request(() -> channel.sendMessage("Could not play: " + exception.getMessage()));
  }
}
