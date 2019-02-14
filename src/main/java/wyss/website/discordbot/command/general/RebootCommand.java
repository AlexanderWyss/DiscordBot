package wyss.website.discordbot.command.general;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import discord4j.core.event.domain.message.MessageCreateEvent;
import wyss.website.discordbot.GuildManager;
import wyss.website.discordbot.command.Command;
import wyss.website.discordbot.command.Help;
import wyss.website.discordbot.music.commands.PlayCommand;

public class RebootCommand extends Command {

  private static final Logger LOGGER = LoggerFactory.getLogger(PlayCommand.class);

  public RebootCommand(GuildManager manager) {
    super(manager, "nick");
  }

  @Override
  public void execute(MessageCreateEvent event) {
    reply(event, "Rebooting...").subscribe();
    try {
      Runtime.getRuntime().exec("sudo reboot now");
    } catch (IOException e) {
      LOGGER.error("Exception: ", e);
    }
  }

  @Override
  public Help getHelp() {
    return new Help("Nickes the bot", "", "name");
  }
}
