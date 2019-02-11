package wyss.website.discordbot.music.commands;

import discord4j.core.event.domain.message.MessageCreateEvent;
import wyss.website.discordbot.GuildManager;
import wyss.website.discordbot.command.Command;
import wyss.website.discordbot.command.Help;

public class NextCommand extends Command {

  public NextCommand(GuildManager manager) {
    super(manager, "next");
  }

  @Override
  public void execute(MessageCreateEvent event) {
    getManager().getGuildMusicManager().getScheduler().next();
  }

  @Override
  public Help getHelp() {
    return new Help("Plays the next song of the queue", "");
  }
}
