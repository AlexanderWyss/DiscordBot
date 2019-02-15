package wyss.website.discordbot.music.playlist;

import discord4j.core.object.entity.User;
import wyss.website.discordbot.GuildManager;

public class PlaylistEditor {

  private User user;
  private Playlist playlist;
  private GuildManager manager;

  public PlaylistEditor(User user, Playlist playlist, GuildManager manager) {
    this.user = user;
    this.playlist = playlist;
    this.manager = manager;
  }

  public void edit() {
  }
}
