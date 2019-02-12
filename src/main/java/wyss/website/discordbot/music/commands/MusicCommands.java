package wyss.website.discordbot.music.commands;

import java.util.ArrayList;
import java.util.List;

import wyss.website.discordbot.GuildManager;
import wyss.website.discordbot.command.Command;
import wyss.website.discordbot.command.Commands;

public class MusicCommands extends Commands {

  List<Command> commands = new ArrayList<>();

  public MusicCommands(GuildManager manager) {
    super(manager);
    commands.add(new PlayCommand(manager));
    commands.add(new MusicPanelCommand(manager));
    commands.add(new ListCommand(manager));
    commands.add(new JumpToCommand(manager));
    commands.add(new NextCommand(manager));
    commands.add(new PreviousCommand(manager));
    commands.add(new PauseCommand(manager));
    commands.add(new ResumeCommand(manager));
    commands.add(new RemoveCommand(manager));
    commands.add(new ClearCommand(manager));
    commands.add(new RepeatCommand(manager));
    commands.add(new VolumeCommand(manager));
    commands.add(new JoinCommand(manager));
    commands.add(new LeaveCommand(manager));
    commands.add(new PlayListCommand(manager));
  }

  @Override
  public String getName() {
    return "Music";
  }

  @Override
  public List<Command> getCommands() {
    return commands;
  }
}
