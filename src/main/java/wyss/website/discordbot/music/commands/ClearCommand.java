package wyss.website.discordbot.music.commands;

import discord4j.core.event.domain.message.MessageCreateEvent;
import wyss.website.discordbot.GuildManager;
import wyss.website.discordbot.command.Command;
import wyss.website.discordbot.command.Help;

public class ClearCommand extends Command {

  public ClearCommand(GuildManager manager) {
    super(manager, "clear");
  }

  @Override
  public void execute(MessageCreateEvent event) {
    getManager().getGuildMusicManager().getScheduler().clear();
  }

  @Override
  public Help getHelp() {
    return new Help("Clears the queue", "");
  }
}
