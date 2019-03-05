package wyss.website.discordbot;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

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

  public static void main(String[] args) throws ParseException, IOException {
    Settings settings = parseSettings(args);
    Bot bot = new Bot(new DiscordClientBuilder(settings.getToken())
        .setInitialPresence(Presence.online(Activity.playing("!Help"))).build());
    bot.login();
  }

  private static Settings parseSettings(String[] args) throws ParseException, IOException {
    Options options = new Options();
    options.addOption("t", "token", true, "Set the discord token");
    DefaultParser parser = new DefaultParser();
    CommandLine cmd = parser.parse(options, args);
    SettingsPersister settingsPersister = new SettingsPersister();
    Settings settings;
    if (cmd.hasOption("token")) {
      settings = new Settings(cmd.getOptionValue("token"));
      settingsPersister.save(settings);
    }
    settings = settingsPersister.loadSettings();
    return settings;
  }
}
