package wyss.website.discordbot.music.commands;

import discord4j.core.event.domain.message.MessageCreateEvent;
import wyss.website.discordbot.GuildManager;
import wyss.website.discordbot.command.Command;
import wyss.website.discordbot.command.Help;
import wyss.website.discordbot.command.Helper;
import wyss.website.discordbot.music.Repeat;
import wyss.website.discordbot.music.TrackScheduler;

public class RepeatCommand extends Command {

  public RepeatCommand(GuildManager manager) {
    super(manager, "repeat");
  }

  @Override
  public void execute(MessageCreateEvent event) {
    TrackScheduler scheduler = getManager().getGuildMusicManager().getScheduler();
    String cutOffCommand = cutOffCommand(event);
    boolean commandExecuted = Helper.commandMapper().map("playlist", param -> scheduler.setRepeat(Repeat.LIST))
        .map("song", param -> scheduler.setRepeat(Repeat.SONG)).map("none", param -> scheduler.setRepeat(Repeat.NONE))
        .noParams().execute(cutOffCommand, "playlist");
    if (!commandExecuted) {
      reply(event, cutOffCommand + "not recognised").subscribe();
    }
  }

  @Override
  public Help getHelp() {
    return new Help("Repeats music depending on the selected option", "", "playlist(default)|song|none");
  }

}
