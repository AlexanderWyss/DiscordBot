package wyss.website.discordbot.command;

import java.util.Collections;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CommandMapper {

  private static final Logger LOGGER = LoggerFactory.getLogger(CommandMapper.class);

  ConcurrentHashMap<String, Consumer<String>> commandMap = new ConcurrentHashMap<>();

  private boolean noParams;

  public CommandMapper map(String identifier, Consumer<String> command) {
    commandMap.put(identifier, command);
    return this;
  }

  public boolean execute(String command, String defaultIdentifier) {
    Matcher matcher = Helper.matcherCaseInsensitive(
        "^" + getCommandPattern(defaultIdentifier != null) + "\\s*(?<params>" + (noParams ? "" : ".+") + ")?", command);
    if (matcher.matches()) {
      String identifier = matcher.group("command");
      if (identifier == null && defaultIdentifier != null) {
        identifier = defaultIdentifier;
      }

      String params = matcher.group("params");
      LOGGER.info("ident: {}, params: {}", identifier, params);
      Consumer<String> consumer = commandMap.get(identifier);
      if (consumer != null) {
        consumer.accept(params);
      } else {
        return false;
      }
      return true;
    } else {
      return false;
    }
  }

  public boolean execute(String command) {
    return execute(command, null);
  }

  private String getCommandPattern(boolean withDefault) {
    return "((?<command>" + Collections.list(commandMap.keys()).stream().collect(Collectors.joining("|")) + ")[\\s$])"
        + (withDefault ? "?" : "");
  }

  public CommandMapper noParams() {
    noParams = true;
    return this;
  }
}
