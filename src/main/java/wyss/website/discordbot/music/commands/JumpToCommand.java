package wyss.website.discordbot.music.commands;

import discord4j.core.event.domain.message.MessageCreateEvent;
import wyss.website.discordbot.GuildManager;
import wyss.website.discordbot.command.Command;
import wyss.website.discordbot.command.Help;

public class JumpToCommand extends Command {

  public JumpToCommand(GuildManager manager) {
    super(manager, "jumpTo");
  }

  @Override
  public void execute(MessageCreateEvent event) {
    String number = cutOffCommand(event);
    try {
      boolean playingSuccesful = getManager().getGuildMusicManager().getScheduler()
          .jumpToSong(Integer.parseInt(number) - 1);
      if (!playingSuccesful) {
        reply(event, number + " is out of range.").subscribe();
      }
    } catch (NumberFormatException e) {
      reply(event, number + " is not a number.").subscribe();
    }
  }

  @Override
  public Help getHelp() {
    return new Help("Jumps to song by number, use !list", "", "number");
  }
}
