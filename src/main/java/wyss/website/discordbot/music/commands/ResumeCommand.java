package wyss.website.discordbot.music.commands;

import discord4j.core.event.domain.message.MessageCreateEvent;
import wyss.website.discordbot.GuildManager;
import wyss.website.discordbot.command.Command;
import wyss.website.discordbot.command.Help;

public class ResumeCommand extends Command {

  public ResumeCommand(GuildManager manager) {
    super(manager, "Resume");
  }

  @Override
  public void execute(MessageCreateEvent event) {
    getManager().getGuildMusicManager().getScheduler().resume();
  }

  @Override
  public Help getHelp() {
    return new Help("Resumes the music playback", "");
  }
}
