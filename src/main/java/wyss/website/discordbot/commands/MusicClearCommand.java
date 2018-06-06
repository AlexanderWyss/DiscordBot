package wyss.website.discordbot.commands;

import java.util.List;

import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import wyss.website.discordbot.DiscordListener;

public class MusicClearCommand extends Command {

  private static final String COMMAND_PATTERN = "clear";

  public MusicClearCommand() {
    super(COMMAND_PATTERN);
  }

  @Override
  public void execute(MessageReceivedEvent event, DiscordListener discordListener, List<String> params) {
    discordListener.getGuildAudioPlayer(event.getGuild()).scheduler.clear();
  }

  @Override
  public String getDescription() {
    return "Clears the playlist";
  }

}
