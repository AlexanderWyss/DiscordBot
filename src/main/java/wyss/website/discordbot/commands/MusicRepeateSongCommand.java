package wyss.website.discordbot.commands;

import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.util.RequestBuffer;
import wyss.website.discordbot.DiscordListener;
import wyss.website.discordbot.GuildMusicManager;

public class MusicRepeateSongCommand implements Command {

  private static final String COMMAND_TEXT = "MusicBot repeateSong ";

  @Override
  public boolean matches(MessageReceivedEvent event, DiscordListener discordListener) {
    return event.getMessage().getFormattedContent().toUpperCase().startsWith(COMMAND_TEXT.toUpperCase());
  }

  @Override
  public void execute(MessageReceivedEvent event, DiscordListener discordListener) {
    String param = event.getMessage().getFormattedContent().substring(COMMAND_TEXT.length()).trim();
    IChannel channel = event.getChannel();
    GuildMusicManager musicManager = discordListener.getGuildAudioPlayer(channel.getGuild());
    boolean value = "y".equals(param);
    musicManager.scheduler.repeateSong(value);
    RequestBuffer.request(() -> channel.sendMessage("Song repeate set to " + value));
  }

  @Override
  public String getCommandPatternDescription() {
    return COMMAND_TEXT + "<y/n>";
  }

  @Override
  public String getDescription() {
    return "Repeate the current Song infinitly";
  }

}
