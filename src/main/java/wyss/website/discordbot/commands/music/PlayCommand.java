package wyss.website.discordbot.commands.music;

import java.util.concurrent.CompletableFuture;

import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;

import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.entity.Message;
import wyss.website.discordbot.GuildManager;
import wyss.website.discordbot.commands.Command;
import wyss.website.discordbot.music.AbstractAudioLoadResultHandler;

public class PlayCommand extends Command {

  public PlayCommand(GuildManager manager) {
    super(manager, "play");
  }

  @Override
  public void execute(MessageCreateEvent event) {
    String url = cutOffCommand(event);
    CompletableFuture<Message> loadingMessage = reply(event, "Loading...").toFuture();
    getManager().getGuildMusicManager().getAudioLoader().play(url, new AbstractAudioLoadResultHandler() {
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
    });
    getManager().getGuildMusicManager().join(event.getMember().get()).subscribe();
  }
}
