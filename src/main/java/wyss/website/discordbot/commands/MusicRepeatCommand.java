package wyss.website.discordbot.commands;

import java.util.List;

import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.util.RequestBuffer;
import wyss.website.discordbot.DiscordListener;
import wyss.website.discordbot.TrackScheduler;

public class MusicRepeatCommand extends Command {

  private static final String COMMAND_PATTERN = "repeatPlaylist";

  public MusicRepeatCommand() {
    super(COMMAND_PATTERN);
  }

  @Override
  public void execute(MessageReceivedEvent event, DiscordListener discordListener, List<String> params) {
    IChannel channel = event.getChannel();
    TrackScheduler scheduler = discordListener.getGuildAudioPlayer(channel.getGuild()).scheduler;
    scheduler.toggleRepeatePlaylist();
    RequestBuffer.request(() -> channel.sendMessage("Playlist repeat set to " + scheduler.isRepeatePlaylistSet()));
  }

  @Override
  public String getDescription() {
    return "Repeat the current playlist infinitely (toggle)";
  }
}
