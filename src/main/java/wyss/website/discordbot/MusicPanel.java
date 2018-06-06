package wyss.website.discordbot;

import org.apache.commons.lang3.time.DurationFormatUtils;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackInfo;

import sx.blah.discord.api.internal.json.objects.EmbedObject;
import sx.blah.discord.handle.impl.obj.ReactionEmoji;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.util.EmbedBuilder;
import sx.blah.discord.util.RequestBuffer;

public class MusicPanel implements Observer {

  private static final String REPEATE = "\uD83D\uDD01";
  private static final String REPEATE_ONE = "\uD83D\uDD02";
  private static final String PLAING = "\u25B6";
  private static final String PAUSED = "\u23F8";
  public static final ReactionEmoji ARROW_BACK = ReactionEmoji.of("\u23EE");
  public static final ReactionEmoji PAUSE_PLAY = ReactionEmoji.of("\u23EF");
  public static final ReactionEmoji ARROW_FORWARD = ReactionEmoji.of("\u23ED");
  public static final ReactionEmoji VOLUME_DOWN = ReactionEmoji.of("\uD83D\uDD09");
  public static final ReactionEmoji VOLUME_UP = ReactionEmoji.of("\uD83D\uDD0A");
  public static final ReactionEmoji REPEATE_EMOJI = ReactionEmoji.of(REPEATE);
  public static final ReactionEmoji REPEATE_ONE_EMOJI = ReactionEmoji.of(REPEATE_ONE);

  private IChannel channel;
  private IMessage sentMessage;
  private DiscordListener discordListener;

  public MusicPanel(IChannel channel, DiscordListener discordListener) {
    this.channel = channel;
    this.discordListener = discordListener;
  }

  public void show() {
    sentMessage = RequestBuffer.request(() -> channel.sendMessage(getMessage())).get();
    registerListeners();

    RequestBuffer.request(() -> sentMessage.addReaction(ARROW_BACK)).get();
    RequestBuffer.request(() -> sentMessage.addReaction(PAUSE_PLAY)).get();
    RequestBuffer.request(() -> sentMessage.addReaction(ARROW_FORWARD)).get();
    RequestBuffer.request(() -> sentMessage.addReaction(VOLUME_DOWN)).get();
    RequestBuffer.request(() -> sentMessage.addReaction(VOLUME_UP)).get();
    RequestBuffer.request(() -> sentMessage.addReaction(REPEATE_EMOJI)).get();
    RequestBuffer.request(() -> sentMessage.addReaction(REPEATE_ONE_EMOJI)).get();
  }

  private void registerListeners() {
    discordListener.getGuildAudioPlayer(channel.getGuild()).scheduler.addListener(this);
    discordListener.getGuildAudioPlayer(channel.getGuild()).addListener(this);
  }

  private void deregisterListeners() {
    discordListener.getGuildAudioPlayer(channel.getGuild()).scheduler.removeListener(this);
    discordListener.getGuildAudioPlayer(channel.getGuild()).removeListener(this);
  }

  @Override
  public void update() {
    RequestBuffer.request(() -> sentMessage = sentMessage.edit(getMessage()));
  }

  private EmbedObject getMessage() {
    GuildMusicManager guildAudioPlayer = discordListener.getGuildAudioPlayer(channel.getGuild());
    TrackScheduler scheduler = guildAudioPlayer.scheduler;
    AudioTrack currentTrack = scheduler.getCurrentTrack();
    EmbedBuilder embedObject = new EmbedBuilder().withTitle("Music Panel");
    if (currentTrack != null) {
      AudioTrackInfo info = currentTrack.getInfo();
      embedObject.appendField("Title", (scheduler.isPaused() ? PAUSED : PLAING) + " " + info.title, false);
      embedObject.appendField("Author", info.author, false);
      embedObject.appendField("Duration", DurationFormatUtils.formatDuration(currentTrack.getDuration(), "mm:ss"),
          false);
    }
    embedObject.appendField("Songs in queue",
        scheduler.getNumberOfSongsPreviouslyInQueue() + " played previously | " + scheduler.getNumberOfSongsInQueue() + " in Queue", false);
    embedObject.appendField("Volume", guildAudioPlayer.getVolume() + "%", false);
    String repeateContent = (scheduler.isRepeatePlaylistSet() ? REPEATE : "")
        + (scheduler.isRepeateSongSet() ? REPEATE_ONE : "");
    embedObject.appendField("Repeat", repeateContent.isEmpty() ? "None" : repeateContent, false);
    return embedObject.build();
  }

  public void shutdown() {
    deregisterListeners();
    sentMessage.delete();
  }
}
