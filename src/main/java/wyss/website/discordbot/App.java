package wyss.website.discordbot;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import sx.blah.discord.api.ClientBuilder;

public class App {

  private static final Logger LOGGER = LoggerFactory.getLogger(App.class);

  public static void main(String[] args) {
    new ClientBuilder().withToken("").registerListeners(new DiscordListener()).login();
  }
}
