package wyss.website.discordbot.command.general;

import java.util.ArrayList;
import java.util.List;

import wyss.website.discordbot.GuildManager;
import wyss.website.discordbot.command.Command;
import wyss.website.discordbot.command.Commands;

public class GeneralCommands extends Commands {

  List<Command> commands = new ArrayList<>();

  public GeneralCommands(GuildManager manager) {
    super(manager);
    commands.add(new HelpCommand(getManager()));
  }

  @Override
  public String getName() {
    return "General";
  }

  @Override
  public List<Command> getCommands() {
    return commands;
  }

}