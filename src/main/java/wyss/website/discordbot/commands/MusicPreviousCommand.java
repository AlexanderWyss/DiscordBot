package wyss.website.discordbot.commands;

import java.util.List;

import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import wyss.website.discordbot.DiscordListener;

public class MusicPreviousCommand extends Command {

  private static final String COMMAND_PATTERN = "previous";

  public MusicPreviousCommand() {
    super(COMMAND_PATTERN);
  }

  @Override
  public void execute(MessageReceivedEvent event, DiscordListener discordListener, List<String> params) {
    discordListener.getGuildAudioPlayer(event.getGuild()).scheduler.previousTrack();
  }

  @Override
  public String getDescription() {
    return "Plays the previous Track from the queue";
  }

}
