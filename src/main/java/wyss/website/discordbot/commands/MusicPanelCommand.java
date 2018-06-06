package wyss.website.discordbot.commands;

import java.util.List;

import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import wyss.website.discordbot.DiscordListener;
import wyss.website.discordbot.MusicPanel;

public class MusicPanelCommand extends Command {

  private static final String COMMAND_PATTERN = "musicpanel";
  private MusicPanel oldPanel = null;

  public MusicPanelCommand() {
    super(COMMAND_PATTERN);
  }

  @Override
  public void execute(MessageReceivedEvent event, DiscordListener discordListener, List<String> params) {
    MusicPanel musicPanel = new MusicPanel(event.getChannel(), discordListener);
    if (oldPanel != null) {
      oldPanel.shutdown();
    }
    oldPanel = musicPanel;
    musicPanel.show();
  }

  @Override
  public String getDescription() {
    return "Shows a Music-Controll-Panel";
  }

}
