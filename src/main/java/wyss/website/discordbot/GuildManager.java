package wyss.website.discordbot;

import discord4j.core.DiscordClient;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.entity.Guild;
import wyss.website.discordbot.music.GuildMusicManager;

public class GuildManager {

  private Guild guild;
  private DiscordClient client;
  private GuildMusicManager guildMusicManager;

  public GuildManager(Guild guild) {
    this.guild = guild;
    client = guild.getClient();
    guildMusicManager = GuildMusicManager.build();
    client.getEventDispatcher().on(MessageCreateEvent.class).filter(this::isGuild)
        .filter(event -> event.getMessage().getContent().map(content -> content.startsWith("!play")).orElse(false))
        .subscribe(event -> play(event, guildMusicManager));
  }

  private boolean isGuild(MessageCreateEvent event) {
    return event.getGuildId().map(id -> guild.getId().equals(id)).orElse(false);
  }

  private void play(MessageCreateEvent event, GuildMusicManager guildMusicManager) {
    String command = event.getMessage().getContent().get();
    if (command.contains(" ")) {
      String url = command.substring(command.indexOf(" ")).trim();
      guildMusicManager.join(event.getMember().get())
          .subscribe(connection -> System.out.println("Connected " + connection));
      guildMusicManager.getAudioLoader().play(url);
    } else {
      System.out.println("no Url");
    }
  }
}
