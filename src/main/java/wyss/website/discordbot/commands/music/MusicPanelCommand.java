package wyss.website.discordbot.commands.music;

import discord4j.core.event.domain.message.MessageCreateEvent;
import wyss.website.discordbot.GuildManager;
import wyss.website.discordbot.commands.Command;
import wyss.website.discordbot.music.MusicPanel;

public class MusicPanelCommand extends Command {

  public MusicPanelCommand(GuildManager manager) {
    super(manager, "musicpanel");
  }

  @Override
  public void execute(MessageCreateEvent event) {
    event.getMessage().getChannel().subscribe(channel -> {
      MusicPanel musicPanel = new MusicPanel(channel, getManager());
      musicPanel.show();
    });
  }

  @Override
  public String getDescription() {
    return "Displays a music control panel.";
  }
}
