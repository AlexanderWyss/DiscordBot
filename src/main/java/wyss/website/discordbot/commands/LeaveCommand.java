package wyss.website.discordbot.commands;

import java.util.List;

import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.obj.IVoiceChannel;
import wyss.website.discordbot.DiscordListener;
import wyss.website.discordbot.TrackScheduler;

public class LeaveCommand extends Command {
  private static final String COMMAND_PATTERN = "Leave";

  public LeaveCommand() {
    super(COMMAND_PATTERN);
  }

  @Override
  public void execute(MessageReceivedEvent event, DiscordListener discordListener, List<String> params) {
    IVoiceChannel channel = event.getClient().getOurUser().getVoiceStateForGuild(event.getGuild()).getChannel();
    if (channel != null) {
      channel.leave();
      TrackScheduler scheduler = discordListener.getGuildAudioPlayer(event.getGuild()).scheduler;
      scheduler.pausePlaying();
      scheduler.clear();
    }
  }

  @Override
  public String getDescription() {
    return "Leaves the Channel";
  }

}
