package wyss.website.discordbot.music.commands;

import discord4j.core.event.domain.message.MessageCreateEvent;
import wyss.website.discordbot.GuildManager;
import wyss.website.discordbot.command.Command;
import wyss.website.discordbot.command.Help;

public class PreviousCommand extends Command {

  public PreviousCommand(GuildManager manager) {
    super(manager, "previous");
  }

  @Override
  public void execute(MessageCreateEvent event) {
    getManager().getGuildMusicManager().getScheduler().previous();
  }

  @Override
  public Help getHelp() {
    return new Help("Plays the previous song of the queue", "");
  }
}
