package wyss.website.discordbot.commands;

import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import wyss.website.discordbot.DiscordListener;

public class MusicContinueCommand implements Command {

	private static final String COMMAND_TEXT = "MusicBot continue";

	@Override
	public boolean matches(MessageReceivedEvent event, DiscordListener discordListener) {
		return event.getMessage().getFormattedContent().equalsIgnoreCase(COMMAND_TEXT);
	}

	@Override
	public void execute(MessageReceivedEvent event, DiscordListener discordListener) {
		discordListener.getGuildAudioPlayer(event.getGuild()).scheduler.continuePlaying();
	}

	@Override
	public String getCommandPatternDescription() {
		return COMMAND_TEXT;
	}

	@Override
	public String getDescription() {
		return "Continues the music playback";
	}

}
