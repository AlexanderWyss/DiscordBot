package wyss.website.discordbot.music.commands;

import java.util.ArrayList;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;

import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.entity.Message;
import wyss.website.discordbot.GuildManager;
import wyss.website.discordbot.command.Command;
import wyss.website.discordbot.command.Help;
import wyss.website.discordbot.command.Helper;
import wyss.website.discordbot.music.AbstractAudioLoadResultHandler;
import wyss.website.discordbot.music.AudioLoader;
import wyss.website.discordbot.music.playlist.Playlist;
import wyss.website.discordbot.music.playlist.PlaylistPersister;

public class PlayListCommand extends Command {

  private static final Logger LOGGER = LoggerFactory.getLogger(PlayListCommand.class);

  public PlayListCommand(GuildManager manager) {
    super(manager, "playlist");
  }

  @Override
  public void execute(MessageCreateEvent event) {
    Helper.commandMapper().map("save", param -> {
      if (param != null && !param.isEmpty()) {
        PlaylistPersister.get().save(event, param, getManager().getGuildMusicManager().getScheduler().getAudioTracks());
      } else {
        reply(event, "Please specify a name.").subscribe();
      }
    })//
        .map("create", param -> PlaylistPersister.get().save(event, param, new ArrayList<>()))//
        .map("list", param -> reply(event, "Playlists", getPlaylistsText()).subscribe())//
        .map("add", param -> {
          String[] params = param.split(" ", 2);
          getPlaylist(event, params[0]).ifPresent(
              playlist -> playlist.add(getManager().getGuildMusicManager().getAudioLoader(), event, params[1].trim()));
        })//
        .map("addList", param -> {
          String[] params = param.split(" ", 2);
          getPlaylist(event, params[0].trim()).ifPresent(playlist -> playlist
              .addList(getManager().getGuildMusicManager().getAudioLoader(), event, params[1].trim()));
        })//
        .map("remove", param -> {
          String[] params = param.split(" ", 2);
          getPlaylist(event, params[0].trim()).ifPresent(playlist -> playlist.remove(event, params[1].trim()));
        })//
        .map("delete", param -> getPlaylist(event, param.trim()).ifPresent(playlist -> playlist.delete(event)))//
        .map("play", param -> play(param, event))//
        .execute(cutOffCommand(event), "list");
  }

  private String getPlaylistsText() {
    return PlaylistPersister.get().getPlaylists().keySet().stream().collect(Collectors.joining(System.lineSeparator()));
  }

  private void play(String playCommand, MessageCreateEvent event) {
    AudioLoader audioLoader = getManager().getGuildMusicManager().getAudioLoader();
    Helper.commandMapper()
        .map("now",
            param -> loadPlaylist(event, param, playlist -> audioLoader.playNow(playlist, new MessageSender(event))))
        .map("next", param -> loadPlaylist(event, param, audioLoader::playNext))
        .map("queue", param -> loadPlaylist(event, param, audioLoader::queue)).map("replace", param -> {
          getManager().getGuildMusicManager().getScheduler().clear();
          loadPlaylist(event, param, audioLoader::playNow);
        }).execute(playCommand, "now");
    getManager().getGuildMusicManager().join(event.getMember().get());
  }

  private void loadPlaylist(MessageCreateEvent event, String name, Consumer<? super Playlist> play) {
    getPlaylist(event, name).ifPresent(play);
  }

  private Optional<Playlist> getPlaylist(MessageCreateEvent event, String name) {
    PlaylistPersister playlistPersister = PlaylistPersister.get();
    Optional<Playlist> playlist = Optional.ofNullable(playlistPersister.getPlaylists().get(name));
    if (!playlist.isPresent()) {
      reply(event, "No playlist found with name: " + name).subscribe();
    }
    return playlist;
  }

  @Override
  public Help getHelp() {
    return new Help("WIP", "", "save", "name");
  }

  private final class MessageSender extends AbstractAudioLoadResultHandler {
    private final CompletableFuture<Message> loadingMessage;

    private MessageSender(MessageCreateEvent event) {
      this.loadingMessage = reply(event, "Loading...").toFuture();
    }

    @Override
    public void loadSuccessful(String url) {
      loadingMessage.thenAccept(message -> message.delete().subscribe());
    }

    @Override
    public void loadFailed(FriendlyException exception, String url) {
      loadingMessage.thenAccept(message -> message.edit(spec -> spec.setContent(exception.getMessage())).subscribe());
    }

    @Override
    public void noMatches(String url) {
      loadingMessage
          .thenAccept(message -> message.edit(spec -> spec.setContent("No results found for: " + url)).subscribe());
    }
  }
}
