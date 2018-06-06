package wyss.website.discordbot.commands;

import java.util.List;

import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import wyss.website.discordbot.DiscordListener;

public class MusicSetVolumeCommand extends Command {

  private static final String COMMAND_PATTERN = "setVolume (\\d+)";
  private static final String COMMAND_PATTERN_DESCRIPTION = "setVolume <volume (0-150)>";

  public MusicSetVolumeCommand() {
    super(COMMAND_PATTERN, COMMAND_PATTERN_DESCRIPTION);
  }

  @Override
  public void execute(MessageReceivedEvent event, DiscordListener discordListener, List<String> params) {
    int volume = Integer.parseInt(params.get(0));
    discordListener.getGuildAudioPlayer(event.getGuild()).setVolume(volume);
  }

  @Override
  public String getDescription() {
    return "Sets the Volume";
  }

}
