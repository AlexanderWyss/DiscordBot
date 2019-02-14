package wyss.website.discordbot.command.general;

import discord4j.core.event.domain.message.MessageCreateEvent;
import wyss.website.discordbot.GuildManager;
import wyss.website.discordbot.command.Command;
import wyss.website.discordbot.command.Help;

public class NickCommand extends Command {

  public NickCommand(GuildManager manager) {
    super(manager, "nick");
  }

  @Override
  public void execute(MessageCreateEvent event) {
    getManager().getGuild().changeSelfNickname(cutOffCommand(event)).subscribe();
  }

  @Override
  public Help getHelp() {
    return new Help("Nickes the bot", "", "name");
  }
}
