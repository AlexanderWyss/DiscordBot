package wyss.website.discordbot.commands;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import wyss.website.discordbot.DiscordListener;

public abstract class Command {

  public static String COMMAND_PREFIX = "!";
  public static String ESCAPED_COMMAND_PREFIX = COMMAND_PREFIX;

  private String regexPattern;
  private String commandPatternText;
  private boolean isSecret;

  public Command(String regexPattern, String commandPatternText, boolean isSecret) {
    this.regexPattern = regexPattern;
    this.commandPatternText = commandPatternText;
    this.isSecret = isSecret;
  }

  public Command(String regexPattern, String commandPatternText) {
    this(regexPattern, commandPatternText, false);
  }

  public Command(String regexPattern, boolean isSecret) {
    this(regexPattern, regexPattern, isSecret);
  }

  public Command(String regexPattern) {
    this(regexPattern, regexPattern, false);
  }

  public boolean executeIfmatches(MessageReceivedEvent event, DiscordListener discordListener) {
    if (!isSecret || discordListener.getAdmins().contains(event.getAuthor().getStringID())) {
      Pattern pattern = Pattern.compile(ESCAPED_COMMAND_PREFIX + regexPattern,
          Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
      Matcher matcher = pattern.matcher(event.getMessage().getContent().trim());
      if (matcher.matches()) {
        int groupCount = matcher.groupCount();
        List<String> params = new ArrayList<>();
        for (int i = 1; i <= groupCount; i++) {
          params.add(matcher.group(i));
        }
        execute(event, discordListener, params);
        return true;
      }
    }
    return false;
  }

  public String getCommandPatternText() {
    return COMMAND_PREFIX + commandPatternText;
  }

  public boolean isSecret() {
    return isSecret;
  }

  public abstract String getDescription();

  protected abstract void execute(MessageReceivedEvent event, DiscordListener discordListener, List<String> params);
}
