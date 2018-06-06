package wyss.website.discordbot.commands;

import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.obj.IChannel;
import wyss.website.discordbot.DiscordListener;
import wyss.website.discordbot.GuildMusicManager;

public class MusicQueueCommand implements Command {

	private static final String COMMAND_TEXT = "MusicBot queue ";

	@Override
	public boolean matches(MessageReceivedEvent event, DiscordListener discordListener) {
		return event.getMessage().getFormattedContent().toUpperCase().startsWith(COMMAND_TEXT.toUpperCase());
	}

	@Override
	public void execute(MessageReceivedEvent event, DiscordListener discordListener) {
		String url = event.getMessage().getFormattedContent().substring(COMMAND_TEXT.length()).trim();
		IChannel channel = event.getChannel();
		GuildMusicManager musicManager = discordListener.getGuildAudioPlayer(channel.getGuild());
		discordListener.getPlayerManager().loadItemOrdered(musicManager, url,
				new DiscordAudioLoadResultHandler(channel, url) {

					@Override
					public void trackLoaded(AudioTrack track) {
						musicManager.scheduler.queue(track);
					}

					@Override
					public void playlistLoaded(AudioPlaylist playlist) {
						musicManager.scheduler.queue(playlist);
					}
				});
	}

	@Override
	public String getCommandPatternDescription() {
		return COMMAND_TEXT + "<url>";
	}

	@Override
	public String getDescription() {
		return "Adds the Song or Playlist to the Queue";
	}

}
