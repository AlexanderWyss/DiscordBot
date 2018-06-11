package wyss.website.discordbot;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Paths;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import sx.blah.discord.api.ClientBuilder;

public class App {

  private static final Logger LOGGER = LoggerFactory.getLogger(App.class);

  public static void main(String[] args) {
    try {
      Settings settings = new SettingsReader(
          Paths.get(App.class.getClassLoader().getResource("ignore/Settings.json").toURI()).toFile()).read();
      LOGGER.debug("Token: {}", settings.getToken());
      new ClientBuilder().withToken(settings.getToken()).registerListeners(new DiscordListener()).login();
    } catch (IOException | URISyntaxException e) {
      LOGGER.error("Starting failed: ", e);
    }
  }
}
