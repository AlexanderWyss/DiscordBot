package wyss.website.discordbot.commands;

import java.util.List;

import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import wyss.website.discordbot.DiscordListener;
import wyss.website.discordbot.GuildMusicManager;

public class MusicNextCommand extends Command {

  private static final String COMMAND_PATTERN = "next";

  public MusicNextCommand() {
    super(COMMAND_PATTERN);
  }

  @Override
  public void execute(MessageReceivedEvent event, DiscordListener discordListener, List<String> params) {
    GuildMusicManager musicManager = discordListener.getGuildAudioPlayer(event.getGuild());
    musicManager.scheduler.nextTrack();
    if (!discordListener.isCurrentVoiceChannelNotEmpty(event.getGuild())) {
      musicManager.scheduler.pausePlaying();
    }
  }

  @Override
  public String getDescription() {
    return "Plays the next Track from the queue";
  }

}
