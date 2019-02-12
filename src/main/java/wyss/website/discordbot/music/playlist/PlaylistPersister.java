package wyss.website.discordbot.music.playlist;

import java.io.BufferedWriter;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import wyss.website.discordbot.music.Audio;

public class PlaylistPersister {

  private static final Logger LOGGER = LoggerFactory.getLogger(PlaylistPersister.class);

  private ConcurrentHashMap<String, Playlist> playlists;
  private Path directory;
  private Path path;

  private PlaylistPersister(Path directory) {
    this.directory = directory;
    this.path = directory.resolve("playlists.json");
  }

  public void loadPlaylists() throws IOException {
    playlists = new ConcurrentHashMap<>();
    if (Files.exists(path)) {
      JSONObject jsonObject = new JSONObject(new String(Files.readAllBytes(path)));
      JSONArray jsonPlaylists = jsonObject.getJSONArray("playlists");
      for (int i = 0; i < jsonPlaylists.length(); i++) {
        try {
          JSONObject jsonPlaylist = jsonPlaylists.getJSONObject(i);
          Playlist playlist;
          playlist = new Playlist(jsonPlaylist.getString("name"));
          JSONArray jsonSongs = jsonPlaylist.getJSONArray("songs");
          playlist.setSongs(getSongsFromJson(jsonSongs));
          playlists.put(playlist.getName(), playlist);
        } catch (PlayListNameException e) {
          LOGGER.warn("Exception: ", e);
        }
      }
    }
  }

  private List<String> getSongsFromJson(JSONArray jsonSongs) {
    List<String> songs = new ArrayList<>();
    for (int j = 0; j < jsonSongs.length(); j++) {
      songs.add(jsonSongs.getString(j));
    }
    return songs;
  }

  public ConcurrentMap<String, Playlist> getPlaylists() {
    return playlists;
  }

  public void save(Playlist playlist) throws IOException {
    playlists.put(playlist.getName(), playlist);
    save();
  }

  public void save() throws IOException {
    if (!Files.exists(path)) {
      Files.createDirectories(directory);
      Files.createFile(path);
    }
    JSONObject jsonObject = new JSONObject();
    jsonObject.put("playlists", getPlayListsAsJson(playlists.values()));
    try (BufferedWriter writer = Files.newBufferedWriter(path)) {
      writer.write(jsonObject.toString(2));
    }
  }

  private JSONArray getPlayListsAsJson(Collection<Playlist> playlists) {
    JSONArray jsonPlaylists = new JSONArray();
    for (Playlist playlist : playlists) {
      jsonPlaylists.put(getPlaylistAsJson(playlist));
    }
    return jsonPlaylists;
  }

  private JSONObject getPlaylistAsJson(Playlist playlist) {
    JSONObject jsonPlaylist = new JSONObject();
    jsonPlaylist.put("name", playlist.getName());
    jsonPlaylist.put("songs", getSongsAsJson(playlist.getSongs()));
    return jsonPlaylist;
  }

  private JSONArray getSongsAsJson(List<String> songs) {
    JSONArray jsonSongs = new JSONArray();
    for (String song : songs) {
      jsonSongs.put(song);
    }
    return jsonSongs;
  }

  private static PlaylistPersister persistor;

  public static PlaylistPersister get() throws URISyntaxException, IOException {
    if (persistor == null) {
      persistor = new PlaylistPersister(getDirectory());
      persistor.loadPlaylists();
    }
    return persistor;
  }

  private static Path getDirectory() throws URISyntaxException {
    return Paths.get(PlaylistPersister.class.getProtectionDomain().getCodeSource().getLocation().toURI()).getParent()
        .resolve("discordbot");
  }

  public void save(String name, List<Audio> audioTracks) throws PlayListNameException, IOException {
    Playlist playlist = new Playlist(name);
    List<String> songs = playlist.getSongs();
    for (Audio audio : audioTracks) {
      songs.add(audio.getInfo().uri);
    }
    save(playlist);
  }
}