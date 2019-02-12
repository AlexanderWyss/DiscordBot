package wyss.website.discordbot.music.playlist;

import java.util.ArrayList;
import java.util.List;

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
}