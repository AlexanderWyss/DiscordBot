package wyss.website.discordbot.commands.playlist;

import java.util.ArrayList;
import java.util.List;

public class Playlist {
  private String name;
  private List<String> songs = new ArrayList<>();

  public Playlist(String name) {
    this.name = name;
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
