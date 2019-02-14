package wyss.website.discordbot.music.playlist;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

import discord4j.core.event.domain.message.MessageCreateEvent;
import wyss.website.discordbot.command.Command;
import wyss.website.discordbot.music.AbstractAudioLoadResultHandler;
import wyss.website.discordbot.music.AudioLoader;

public class Playlist {
  private String name;
  private List<String> songs = new ArrayList<>();

  public Playlist(String name) throws PlayListNameException {
    this.name = name.trim();
    if (this.name.contains(" ")) {
      throw new PlayListNameException("Name must not conatain whitespace.");
    }
  }

  public List<String> getSongs() {
    return songs;
  }

  public void setSongs(List<String> songs) {
    this.songs = songs;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public void add(AudioLoader loader, MessageCreateEvent event, String url) {
    loader.load(url, new AbstractAudioLoadResultHandler() {
      @Override
      public void trackLoaded(AudioTrack track, String url) {
        getSongs().add(track.getInfo().uri);
        save(event);
        Command.reply(event, track.getInfo().title + " added.").subscribe();
      }

      @Override
      public void playlistLoaded(AudioPlaylist playlist, String url) {
        getSongs().addAll(playlist.getTracks().stream().map(track -> track.getInfo().uri).collect(Collectors.toList()));
        save(event);
        Command.reply(event, "Songs added.").subscribe();
      }

      @Override
      public void noMatches(String url) {
        Command.reply(event, "No matches found for: " + url).subscribe();
      }

      @Override
      public void loadFailed(FriendlyException exception, String url) {
        Command.reply(event, exception.getMessage()).subscribe();
      }
    });
  }

  public void addList(AudioLoader loader, MessageCreateEvent event, String url) {
    loader.load(url, new AbstractAudioLoadResultHandler() {
      @Override
      public void trackLoaded(AudioTrack track, String url) {
        Command.reply(event, url + " is not a playlist.").subscribe();
      }

      @Override
      public void playlistLoaded(AudioPlaylist playlist, String url) {
        getSongs().add(url);
        save(event);
        Command.reply(event, "Playlist added.").subscribe();
      }

      @Override
      public void noMatches(String url) {
        Command.reply(event, "No matches found for: " + url).subscribe();
      }

      @Override
      public void loadFailed(FriendlyException exception, String url) {
        Command.reply(event, exception.getMessage()).subscribe();
      }
    });
  }

  private void save(MessageCreateEvent event) {
    try {
      PlaylistPersister.get().save();
    } catch (IOException e) {
      Command.reply(event, "Save failed.").subscribe();
    }
  }

  public void delete(MessageCreateEvent event) {
    try {
      PlaylistPersister.get().getPlaylists().remove(name);
      PlaylistPersister.get().save();
    } catch (IOException e) {
      Command.reply(event, "Save failed.").subscribe();
    }
  }

  public void remove(MessageCreateEvent event, String name) {
    try {
      songs.remove(name);
      PlaylistPersister.get().save();
    } catch (IOException e) {
      Command.reply(event, "Save failed.").subscribe();
    }
  }
}