package wyss.website.discordbot;

import java.io.BufferedWriter;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import wyss.website.discordbot.music.playlist.PlaylistPersister;

public abstract class Persister {

  private static final Logger LOGGER = LoggerFactory.getLogger(Persister.class);

  private Path directory;
  private Path path;

  public Persister(Path directory, String fileName) {
    this.directory = directory;
    this.path = directory.resolve(fileName);
  }

  public Persister(String fileName) {
    this(getDefaultDirectory(), fileName);
  }

  public static Path getDefaultDirectory() {
    try {
      Path directory = Paths.get(PlaylistPersister.class.getProtectionDomain().getCodeSource().getLocation().toURI())
          .getParent().resolve("discordbot");
      LOGGER.info(directory.toString());
      return directory;
    } catch (URISyntaxException e) {
      LOGGER.error("Exception: ", e);
      return null;
    }
  }

  protected void createFileIfNotExists() throws IOException {
    if (!Files.exists(path)) {
      Files.createDirectories(directory);
      Files.createFile(path);
    }
  }

  protected BufferedWriter writer() throws IOException {
    return Files.newBufferedWriter(getPath());
  }

  protected void write(JSONObject jsonObject) throws IOException {
    String content = jsonObject.toString(2);
    write(content);
  }

  protected void write(String content) throws IOException {
    createFileIfNotExists();
    try (BufferedWriter writer = writer()) {
      writer.write(content);
    }
  }

  protected String readFile() throws IOException {
    return new String(Files.readAllBytes(getPath()));
  }

  protected Path getPath() {
    return path;
  }

  protected Path getDirectory() {
    return directory;
  }
}
