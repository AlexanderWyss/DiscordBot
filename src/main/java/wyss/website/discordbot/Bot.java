package wyss.website.discordbot;

import java.util.concurrent.ConcurrentHashMap;

import discord4j.core.DiscordClient;
import discord4j.core.DiscordClientBuilder;
import discord4j.core.event.domain.guild.GuildCreateEvent;
import discord4j.core.object.presence.Activity;
import discord4j.core.object.presence.Presence;
import discord4j.core.object.util.Snowflake;

public class Bot {

  private DiscordClient client;
  private ConcurrentHashMap<Snowflake, GuildManager> guildManagers = new ConcurrentHashMap<>();

  public Bot(DiscordClient client) {
    this.client = client;
    createGuildManagers();
  }

  private void createGuildManagers() {
    client.getEventDispatcher().on(GuildCreateEvent.class).map(GuildCreateEvent::getGuild).subscribe(guild -> {
      guildManagers.putIfAbsent(guild.getId(), new GuildManager(guild));
    });
  }

  private void login() {
    client.login().block();
  }

  public static void main(String[] args) {
    Bot bot = new Bot(new DiscordClientBuilder("NDUzNDg0MjUwNTMzMTk5ODcz.D0B8aw.Rd1VgvMYTikvEdEDinsVpaa2x5E")
        .setInitialPresence(Presence.online(Activity.playing("!Help"))).build());
    bot.login();
  }
}
