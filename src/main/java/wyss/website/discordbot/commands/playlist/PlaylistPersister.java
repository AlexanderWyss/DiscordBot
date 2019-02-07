package wyss.website.discordbot.commands.playlist;

import java.io.BufferedWriter;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import org.json.JSONArray;
import org.json.JSONObject;

public class PlaylistPersister {

  private ConcurrentHashMap<String, Playlist> playlists;
  private Path directory;
  private Path path;

  public PlaylistPersister(Path directory) {
    this.directory = directory;
    this.path = directory.resolve("playlists.json");
  }

  public void loadPlaylists() throws URISyntaxException, IOException {
    playlists = new ConcurrentHashMap<>();
    if (Files.exists(path)) {
      JSONObject jsonObject = new JSONObject(new String(Files.readAllBytes(path)));
      JSONArray jsonPlaylists = jsonObject.getJSONArray("playlists");
      for (int i = 0; i < jsonPlaylists.length(); i++) {
        JSONObject jsonPlaylist = jsonPlaylists.getJSONObject(i);
        Playlist playlist = new Playlist(jsonPlaylist.getString("name"));
        JSONArray jsonSongs = jsonPlaylist.getJSONArray("songs");
        playlist.setSongs(getSongsFromJson(jsonSongs));
        playlists.put(playlist.getName(), playlist);
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

  public ConcurrentHashMap<String, Playlist> getPlaylists() {
    return playlists;
  }

  public void save(Playlist playlist) throws URISyntaxException, IOException {
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
}
