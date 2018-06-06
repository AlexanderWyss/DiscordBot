package wyss.website.discordbot.commands;

import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import wyss.website.discordbot.DiscordListener;
import wyss.website.discordbot.MusicPanel;

public class MusicPanelCommand implements Command {

  private static final String COMMAND_TEXT = "Musicbot Panel";
  private MusicPanel oldPanel = null;

  @Override
  public boolean matches(MessageReceivedEvent event, DiscordListener discordListener) {
    return event.getMessage().getFormattedContent().equalsIgnoreCase(COMMAND_TEXT);
  }

  @Override
  public void execute(MessageReceivedEvent event, DiscordListener discordListener) {
    MusicPanel musicPanel = new MusicPanel(event.getChannel(), discordListener);
    if (oldPanel != null) {
      oldPanel.shutdown();
    }
    oldPanel = musicPanel;
    musicPanel.show();
  }

  @Override
  public String getCommandPatternDescription() {
    return COMMAND_TEXT;
  }

  @Override
  public String getDescription() {
    return "Shows a Music-Controll-Panel";
  }

}
