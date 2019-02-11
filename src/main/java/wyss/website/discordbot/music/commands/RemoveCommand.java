package wyss.website.discordbot.music.commands;

import discord4j.core.event.domain.message.MessageCreateEvent;
import wyss.website.discordbot.GuildManager;
import wyss.website.discordbot.command.Command;
import wyss.website.discordbot.command.Help;

public class RemoveCommand extends Command {

  public RemoveCommand(GuildManager manager) {
    super(manager, "remove");
  }

  @Override
  public void execute(MessageCreateEvent event) {
    getManager().getGuildMusicManager().getScheduler().removeCurrentSong();
  }

  @Override
  public Help getHelp() {
    return new Help("Removes the current song from the queue", "");
  }
}
