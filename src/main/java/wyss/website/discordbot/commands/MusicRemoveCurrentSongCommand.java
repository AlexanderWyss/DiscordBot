package wyss.website.discordbot.commands;

import java.util.List;

import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.obj.IChannel;
import wyss.website.discordbot.DiscordListener;
import wyss.website.discordbot.GuildMusicManager;

public class MusicRemoveCurrentSongCommand extends Command {

  private static final String COMMAND_PATTERN = "remove";

  public MusicRemoveCurrentSongCommand() {
    super(COMMAND_PATTERN);
  }

  @Override
  public void execute(MessageReceivedEvent event, DiscordListener discordListener, List<String> params) {
    IChannel channel = event.getChannel();
    GuildMusicManager musicManager = discordListener.getGuildAudioPlayer(channel.getGuild());
    musicManager.scheduler.removeCurrentSong();
  }

  @Override
  public String getDescription() {
    return "Removes the current song from the queue";
  }
}
