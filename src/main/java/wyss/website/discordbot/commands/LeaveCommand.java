package wyss.website.discordbot.commands;

import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.obj.IVoiceChannel;
import wyss.website.discordbot.DiscordListener;
import wyss.website.discordbot.TrackScheduler;

public class LeaveCommand implements Command {

	private static final String COMMAND_TEXT = "Leave me Bot";

	@Override
	public boolean matches(MessageReceivedEvent event, DiscordListener discordListener) {
		return event.getMessage().getFormattedContent().equalsIgnoreCase(COMMAND_TEXT);
	}

	@Override
	public void execute(MessageReceivedEvent event, DiscordListener discordListener) {
		IVoiceChannel channel = event.getClient().getOurUser().getVoiceStateForGuild(event.getGuild()).getChannel();
		if (channel != null) {
			channel.leave();
			TrackScheduler scheduler = discordListener.getGuildAudioPlayer(event.getGuild()).scheduler;
			scheduler.pausePlaying();
			scheduler.clear();
		}
	}

	@Override
	public String getCommandPatternDescription() {
		return COMMAND_TEXT;
	}

	@Override
	public String getDescription() {
		return "Leaves the Channel";
	}

}
