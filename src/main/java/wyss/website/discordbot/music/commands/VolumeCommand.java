package wyss.website.discordbot.music.commands;

import discord4j.core.event.domain.message.MessageCreateEvent;
import wyss.website.discordbot.GuildManager;
import wyss.website.discordbot.command.Command;
import wyss.website.discordbot.command.Help;

public class VolumeCommand extends Command {

  public VolumeCommand(GuildManager manager) {
    super(manager, "setVolume");
  }

  @Override
  public void execute(MessageCreateEvent event) {
    String volume = cutOffCommand(event);
    try {
      getManager().getGuildMusicManager().getScheduler().setVolume(Integer.parseInt(volume));
    } catch (NumberFormatException e) {
      reply(event, volume + " is not a number.").subscribe();
    }
  }

  @Override
  public Help getHelp() {
    return new Help("Sets the volume", "", "volume (0-100)");
  }
}
