package wyss.website.discordbot.music.commands;

import discord4j.core.event.domain.message.MessageCreateEvent;
import wyss.website.discordbot.GuildManager;
import wyss.website.discordbot.command.Command;
import wyss.website.discordbot.command.Help;
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
  public Help getHelp() {
    return new Help("Displays a music control panel.", "");
  }
}
