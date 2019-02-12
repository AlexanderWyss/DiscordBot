package wyss.website.discordbot.music.commands;

import java.io.IOException;
import java.net.URISyntaxException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import discord4j.core.event.domain.message.MessageCreateEvent;
import wyss.website.discordbot.GuildManager;
import wyss.website.discordbot.command.Command;
import wyss.website.discordbot.command.Help;
import wyss.website.discordbot.command.Helper;
import wyss.website.discordbot.music.playlist.PlayListNameException;
import wyss.website.discordbot.music.playlist.PlaylistPersister;

public class PlayListCommand extends Command {

  private static final Logger LOGGER = LoggerFactory.getLogger(PlayListCommand.class);

  public PlayListCommand(GuildManager manager) {
    super(manager, "playlist");
  }

  @Override
  public void execute(MessageCreateEvent event) {
    Helper.commandMapper().map("save", param -> {
      try {
        PlaylistPersister.get().save(param, getManager().getGuildMusicManager().getScheduler().getAudioTracks());
      } catch (PlayListNameException e) {
        reply(event, e.getMessage()).subscribe();
      } catch (IOException | URISyntaxException e) {
        reply(event, "Save failed.").subscribe();
        LOGGER.error("Exception: ", e);
      }
    }).map("help", param -> printHelp()).execute(cutOffCommand(event), "help");
    getManager().getGuildMusicManager().join(event.getMember().get());
  }

  private void printHelp() {
    // TODO
  }

  @Override
  public Help getHelp() {
    return new Help("WIP", "", "save", "name");
  }
}
