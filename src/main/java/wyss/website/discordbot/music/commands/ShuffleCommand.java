package wyss.website.discordbot.music.commands;

import discord4j.core.event.domain.message.MessageCreateEvent;
import wyss.website.discordbot.GuildManager;
import wyss.website.discordbot.command.Command;
import wyss.website.discordbot.command.Help;
import wyss.website.discordbot.command.Helper;
import wyss.website.discordbot.music.TrackScheduler;

public class ShuffleCommand extends Command {

  public ShuffleCommand(GuildManager manager) {
    super(manager, "shuffle");
  }

  @Override
  public void execute(MessageCreateEvent event) {
    TrackScheduler scheduler = getManager().getGuildMusicManager().getScheduler();
    Helper.commandMapper()//
        .map("on", param -> scheduler.setShuffel(true))//
        .map("off", param -> scheduler.setShuffel(false))//
        .execute(cutOffCommand(event), "on");
  }

  @Override
  public Help getHelp() {
    return new Help("Sets shuffle on or off", "", "on(default)|off");
  }

}
