package wyss.website.discordbot.commands;

import java.util.List;

import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.util.RequestBuffer;
import wyss.website.discordbot.DiscordListener;
import wyss.website.discordbot.TrackScheduler;

public class MusicRepeatSongCommand extends Command {

  private static final String COMMAND_PATTERN = "repeatSong";

  public MusicRepeatSongCommand() {
    super(COMMAND_PATTERN);
  }

  @Override
  public void execute(MessageReceivedEvent event, DiscordListener discordListener, List<String> params) {
    IChannel channel = event.getChannel();
    TrackScheduler scheduler = discordListener.getGuildAudioPlayer(channel.getGuild()).scheduler;
    scheduler.toggleRepeateSong();
    RequestBuffer.request(() -> channel.sendMessage("Song repeat set to " + scheduler.isRepeateSongSet()));
  }

  @Override
  public String getDescription() {
    return "Repeat the current song infinitely (toggle)";
  }

}
