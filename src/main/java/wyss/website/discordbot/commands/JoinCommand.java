package wyss.website.discordbot.commands;

import java.util.List;

import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.obj.IVoiceChannel;
import sx.blah.discord.handle.obj.IVoiceState;
import sx.blah.discord.util.RequestBuffer;
import wyss.website.discordbot.DiscordListener;

public class JoinCommand extends Command {
  private static final String COMMAND_PATTERN = "Join";

  public JoinCommand() {
    super(COMMAND_PATTERN);
  }

  @Override
  public void execute(MessageReceivedEvent event, DiscordListener discordListener, List<String> params) {
    IVoiceState voiceState = event.getAuthor().getVoiceStateForGuild(event.getGuild());
    IVoiceChannel channel;
    if (voiceState != null && (channel = voiceState.getChannel()) != null) {
      channel.join();
    } else {
      RequestBuffer.request(() -> event.getChannel().sendMessage("Could not join Channel."));
    }
  }

  @Override
  public String getDescription() {
    return "Joins your current Channel";
  }

}
