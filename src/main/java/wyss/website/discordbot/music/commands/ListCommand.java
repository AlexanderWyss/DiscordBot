package wyss.website.discordbot.music.commands;

import java.util.List;

import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.entity.Message;
import wyss.website.discordbot.GuildManager;
import wyss.website.discordbot.command.Command;
import wyss.website.discordbot.command.Help;
import wyss.website.discordbot.music.Audio;
import wyss.website.discordbot.music.TrackScheduler;

public class ListCommand extends Command {

  public ListCommand(GuildManager manager) {
    super(manager, "list");
  }

  @Override
  public void execute(MessageCreateEvent event) {
    TrackScheduler scheduler = getManager().getGuildMusicManager().getScheduler();
    StringBuilder content = new StringBuilder();
    List<Audio> audioTracks = scheduler.getAudioTracks();
    int messageNumber = 1;
    for (int i = 0; i < audioTracks.size(); i++) {
      Audio audio = audioTracks.get(i);
      String title = getTitle(i + 1, audio);
      if ((content.length() + title.length()) > maxContentLength()) {
        send(event, messageNumber++, content.toString());
        content = new StringBuilder();
      }
      content.append(title);
    }
    send(event, messageNumber, content.toString());
  }

  private void send(MessageCreateEvent event, int messageNumber, String content) {
    reply(event, spec -> spec.setEmbed(embed -> embed.setTitle(messageNumber + ". List").setDescription(content)))
        .subscribe();
  }

  private int maxContentLength() {
    return Message.MAX_CONTENT_LENGTH - 100;
  }

  private String getTitle(int number, Audio audio) {
    StringBuilder title = new StringBuilder();
    title.append(String.valueOf(number)).append(". ").append(audio.getInfo().title).append(System.lineSeparator());
    return title.toString();
  }

  @Override
  public Help getHelp() {
    return new Help("Shows you the songs currently in the queue", "");
  }
}
