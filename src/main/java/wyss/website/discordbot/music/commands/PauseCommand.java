package wyss.website.discordbot.music.commands;

import discord4j.core.event.domain.message.MessageCreateEvent;
import wyss.website.discordbot.GuildManager;
import wyss.website.discordbot.command.Command;
import wyss.website.discordbot.command.Help;

public class PauseCommand extends Command {

  public PauseCommand(GuildManager manager) {
    super(manager, "pause");
  }

  @Override
  public void execute(MessageCreateEvent event) {
    getManager().getGuildMusicManager().getScheduler().pause();
  }

  @Override
  public Help getHelp() {
    return new Help("Pauses the music playback", "");
  }
}
