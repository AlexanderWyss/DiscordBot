package wyss.website.discordbot.commands;

import java.util.List;
import java.util.Map;

import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.obj.IChannel;
import wyss.website.discordbot.DiscordListener;
import wyss.website.discordbot.MusicPanel;

public class MusicPanelCommand extends Command {

  private static final String COMMAND_PATTERN = "musicpanel";
  private Map<IChannel, MusicPanel> oldPanels = null;

  public MusicPanelCommand() {
    super(COMMAND_PATTERN);
  }

  @Override
  public void execute(MessageReceivedEvent event, DiscordListener discordListener, List<String> params) {
    IChannel channel = event.getChannel();
    MusicPanel musicPanel = new MusicPanel(channel, discordListener);
    if (oldPanels.containsKey(channel)) {
      oldPanels.get(channel).shutdown();
    }
    oldPanels.put(channel, musicPanel);
    musicPanel.show();
  }

  @Override
  public String getDescription() {
    return "Shows a Music-Controll-Panel";
  }

  public void deleteAll() {
    oldPanels.values().forEach(MusicPanel::shutdown);
  }

}
