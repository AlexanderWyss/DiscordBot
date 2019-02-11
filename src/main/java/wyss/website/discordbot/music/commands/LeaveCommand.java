package wyss.website.discordbot.music.commands;

import discord4j.core.event.domain.message.MessageCreateEvent;
import wyss.website.discordbot.GuildManager;
import wyss.website.discordbot.command.Command;
import wyss.website.discordbot.command.Help;

public class LeaveCommand extends Command {

  public LeaveCommand(GuildManager manager) {
    super(manager, "leave");
  }

  @Override
  public void execute(MessageCreateEvent event) {
    getManager().getGuildMusicManager().leave();
  }

  @Override
  public Help getHelp() {
    return new Help("Leaves the voicechannel", "");
  }
}
