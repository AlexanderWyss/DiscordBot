package wyss.website.discordbot.commands;

public class Help {

  private String shortDescription;
  private String longDescription;
  private String[] parameters;

  public Help(String shortDescription, String longDescription, String... parameters) {
    this.shortDescription = shortDescription;
    this.longDescription = longDescription;
    this.parameters = parameters;
  }

  public String getShortDescription() {
    return shortDescription;
  }

  public String getLongDescription() {
    return longDescription;
  }

  public String[] getParameters() {
    return parameters;
  }
}
