package wyss.website.discordbot;

import java.io.IOException;
import java.nio.file.Files;

import org.json.JSONObject;

public class SettingsPersister extends Persister {

  public SettingsPersister() {
    super("settings.json");
  }

  public void save(Settings settings) throws IOException {
    JSONObject jsonObject = new JSONObject();
    jsonObject.put("token", settings.getToken());
    write(jsonObject);
  }

  public Settings loadSettings() throws IOException {
    Settings settings = new Settings();
    if (Files.exists(getPath())) {
      JSONObject jsonObject = new JSONObject(readFile());
      settings.setToken(jsonObject.getString("token"));
    }
    return settings;
  }
}
