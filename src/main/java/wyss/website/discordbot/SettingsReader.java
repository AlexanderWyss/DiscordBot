package wyss.website.discordbot;

import java.io.IOException;
import java.io.InputStream;

import com.fasterxml.jackson.databind.ObjectMapper;

public class SettingsReader {

  private InputStream inputStream;

  public SettingsReader(InputStream inputStream) {
    this.inputStream = inputStream;
  }

  public Settings read() throws IOException {
    ObjectMapper mapper = new ObjectMapper();
    return mapper.readValue(inputStream, Settings.class);
  }
}
