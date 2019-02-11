package wyss.website.discordbot.commands.music;

import java.util.concurrent.CompletableFuture;

import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;

import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.entity.Message;
import wyss.website.discordbot.GuildManager;
import wyss.website.discordbot.commands.Command;
import wyss.website.discordbot.commands.Help;
import wyss.website.discordbot.commands.Helper;
import wyss.website.discordbot.music.AbstractAudioLoadResultHandler;
import wyss.website.discordbot.music.AudioLoader;

public class PlayCommand extends Command {

  public PlayCommand(GuildManager manager) {
    super(manager, "play");
  }

  @Override
  public void execute(MessageCreateEvent event) {
    AudioLoader audioLoader = getManager().getGuildMusicManager().getAudioLoader();
    Helper.commandMapper().map("now", param -> audioLoader.playNow(param, new MessageSender(param, event)))
        .map("next", param -> audioLoader.playNext(param, new MessageSender(param, event)))
        .map("queue", param -> audioLoader.queue(param, new MessageSender(param, event))).map("replace", param -> {
          getManager().getGuildMusicManager().getScheduler().clear();
          audioLoader.playNow(param, new MessageSender(param, event));
        }).execute(cutOffCommand(event), "now");
    getManager().getGuildMusicManager().join(event.getMember().get()).subscribe();
  }

  private final class MessageSender extends AbstractAudioLoadResultHandler {
    private final String url;
    private final CompletableFuture<Message> loadingMessage;

    private MessageSender(String url, MessageCreateEvent event) {
      this.url = url;
      this.loadingMessage = reply(event, "Loading...").toFuture();
    }

    @Override
    public void loadSuccessful() {
      loadingMessage.thenAccept(message -> message.delete().subscribe());
    }

    @Override
    public void loadFailed(FriendlyException exception) {
      loadingMessage.thenAccept(message -> message.edit(spec -> spec.setContent(exception.getMessage())).subscribe());
    }

    @Override
    public void noMatches() {
      loadingMessage
          .thenAccept(message -> message.edit(spec -> spec.setContent("No results found for: " + url)).subscribe());
    }
  }

  @Override
  public Help getHelp() {
    return new Help(
        "Adds a song or playlist to the current playlist at a position depending on the first parameter."
            + " The identifier can be an URL or \"ytsearch:<query>\"",
        "", "now(default)|next|queue|replace", "identifier");
  }

}
