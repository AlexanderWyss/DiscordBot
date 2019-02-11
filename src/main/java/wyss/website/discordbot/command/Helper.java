package wyss.website.discordbot.command;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Helper {

  private Helper() {
  }

  public static Matcher matcherCaseInsensitive(String pattern, String string) {
    return Pattern.compile(pattern, Pattern.CASE_INSENSITIVE).matcher(string);
  }

  public static CommandMapper commandMapper() {
    return new CommandMapper();
  }
}
