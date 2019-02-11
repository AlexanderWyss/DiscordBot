package wyss.website.discordbot.music.commands;

import discord4j.core.event.domain.message.MessageCreateEvent;
import wyss.website.discordbot.GuildManager;
import wyss.website.discordbot.command.Command;
import wyss.website.discordbot.command.Help;

public class JoinCommand extends Command {

  public JoinCommand(GuildManager manager) {
    super(manager, "join");
  }

  @Override
  public void execute(MessageCreateEvent event) {
    event.getMessage().getUserMentions().flatMap(user -> user.asMember(getManager().getGuild().getId()))
        .defaultIfEmpty(event.getMember().get()).next()
        .subscribe(member -> getManager().getGuildMusicManager().join(member));
  }

  @Override
  public Help getHelp() {
    return new Help("Joins you or the mentioned user", "", "user(Optional)");
  }
}
