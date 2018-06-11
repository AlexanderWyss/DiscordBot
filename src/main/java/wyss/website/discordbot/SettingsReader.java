package wyss.website.discordbot;

import java.io.File;
import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;

public class SettingsReader {

  private File file;

  public SettingsReader(File file) {
    this.file = file;
  }

  public Settings read() throws IOException {
    ObjectMapper mapper = new ObjectMapper();
    return mapper.readValue(file, Settings.class);
  }
}
