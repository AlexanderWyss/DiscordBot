package wyss.website.discordbot;

import sx.blah.discord.api.ClientBuilder;

public class App {
  private static final String TOKEN = "***REMOVED***";

  public static void main(String[] args) {
    new ClientBuilder().withToken(TOKEN).registerListeners(new DiscordListener()).login();
  }
}
