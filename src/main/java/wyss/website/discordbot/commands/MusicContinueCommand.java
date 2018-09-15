package wyss.website.discordbot.commands;

import java.util.List;

import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import wyss.website.discordbot.DiscordListener;

public class MusicContinueCommand extends Command {

  private static final String COMMAND_PATTERN = "resume";

  public MusicContinueCommand() {
    super(COMMAND_PATTERN);
  }

  @Override
  public void execute(MessageReceivedEvent event, DiscordListener discordListener, List<String> params) {
    if (discordListener.isCurrentVoiceChannelNotEmpty(event.getGuild())) {
      discordListener.getGuildAudioPlayer(event.getGuild()).scheduler.continuePlaying();
    }
  }

  @Override
  public String getDescription() {
    return "Resumes the music playback";
  }

}
