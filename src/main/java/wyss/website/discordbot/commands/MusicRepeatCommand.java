package wyss.website.discordbot.commands;

import java.util.List;

import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.util.RequestBuffer;
import wyss.website.discordbot.DiscordListener;
import wyss.website.discordbot.GuildMusicManager;

public class MusicRepeatCommand extends Command {

  private static final String COMMAND_PATTERN = "repeat (.)";
  private static final String COMMAND_PATTERN_DESCRIPTION = "repeat <y/n>";

  public MusicRepeatCommand() {
    super(COMMAND_PATTERN, COMMAND_PATTERN_DESCRIPTION);
  }

  @Override
  public void execute(MessageReceivedEvent event, DiscordListener discordListener, List<String> params) {
    String param = params.get(0);
    IChannel channel = event.getChannel();
    GuildMusicManager musicManager = discordListener.getGuildAudioPlayer(channel.getGuild());
    boolean value = "y".equals(param);
    musicManager.scheduler.setRepeatePlaylist(value);
    RequestBuffer.request(() -> channel.sendMessage("Playlist repeate set to " + value));
  }

  @Override
  public String getDescription() {
    return "Repeat the current playlist infinitely";
  }

}
