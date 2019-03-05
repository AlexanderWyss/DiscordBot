package wyss.website.discordbot;

public class Settings {
  private String token;

  public Settings(String token) {
    this.token = token;
  }

  public Settings() {
  }

  public String getToken() {
    return token;
  }

  public void setToken(String token) {
    this.token = token;
  }
}
