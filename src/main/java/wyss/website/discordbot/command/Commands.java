package wyss.website.discordbot.command;

import java.util.List;

import wyss.website.discordbot.GuildManager;

public abstract class Commands {

  private GuildManager manager;

  public Commands(GuildManager manager) {
    this.manager = manager;
  }

  public abstract String getName();

  public abstract List<Command> getCommands();

  protected GuildManager getManager() {
    return manager;
  }
}
