package wyss.website.discordbot.commands;

import java.util.List;

import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import wyss.website.discordbot.DiscordListener;

public class MusicPauseCommand extends Command {

  private static final String COMMAND_PATTERN = "pause";

  public MusicPauseCommand() {
    super(COMMAND_PATTERN);
  }

  @Override
  public void execute(MessageReceivedEvent event, DiscordListener discordListener, List<String> params) {
    discordListener.getGuildAudioPlayer(event.getGuild()).scheduler.pausePlaying();
  }

  @Override
  public String getDescription() {
    return "Pauses the music playback";
  }

}
