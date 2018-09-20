package wyss.website.discordbot;

import java.io.IOException;

import sx.blah.discord.api.ClientBuilder;

public class App {

  public static void main(String[] args) throws IOException {
    Settings settings = new SettingsReader(App.class.getClassLoader().getResource("ignore/Settings.json").openStream())
        .read();
    new ClientBuilder().withToken(settings.getToken()).registerListeners(new DiscordListener()).login();
  }
}
