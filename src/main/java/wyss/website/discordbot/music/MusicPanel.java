package wyss.website.discordbot.music;

import java.time.Duration;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

import org.apache.commons.lang3.time.DurationFormatUtils;

import com.sedmelluq.discord.lavaplayer.track.AudioTrackInfo;

import discord4j.core.event.domain.message.ReactionAddEvent;
import discord4j.core.event.domain.message.ReactionRemoveEvent;
import discord4j.core.object.entity.Message;
import discord4j.core.object.entity.MessageChannel;
import discord4j.core.object.reaction.ReactionEmoji;
import discord4j.core.object.util.Snowflake;
import discord4j.core.spec.EmbedCreateSpec;
import reactor.core.Disposable;
import reactor.core.publisher.Flux;
import wyss.website.discordbot.GuildManager;

public class MusicPanel implements Observer {

  private static Map<Snowflake, MusicPanel> musicpanels = new ConcurrentHashMap<>();

  private static final String TITLE = "Music Panel";
  private static final String REPEATE_LIST = "\uD83D\uDD01";
  private static final String REPEATE_SONG = "\uD83D\uDD02";
  private static final String PLAYING = "\u25B6";
  private static final String PAUSED = "\u23F8";
  private static final String SHUFFLE = "\uD83D\uDD00";
  private static final ReactionEmoji ARROW_BACK = ReactionEmoji.unicode("\u23EE");
  private static final ReactionEmoji PAUSE_PLAY = ReactionEmoji.unicode("\u23EF");
  private static final ReactionEmoji ARROW_FORWARD = ReactionEmoji.unicode("\u23ED");
  private static final ReactionEmoji VOLUME_DOWN = ReactionEmoji.unicode("\uD83D\uDD09");
  private static final ReactionEmoji VOLUME_UP = ReactionEmoji.unicode("\uD83D\uDD0A");
  private static final ReactionEmoji REPEATE_LIST_EMOJI = ReactionEmoji.unicode(REPEATE_LIST);
  private static final ReactionEmoji REPEATE_SONG_EMOJI = ReactionEmoji.unicode(REPEATE_SONG);
  private static final ReactionEmoji SHUFFLE_EMOJI = ReactionEmoji.unicode(SHUFFLE);
  private static final ReactionEmoji REMOVE = ReactionEmoji.unicode("\u274C");

  private MessageChannel channel;
  private GuildManager manager;

  private Map<ReactionEmoji, Consumer<GuildMusicManager>> controls = createControls();
  private CompletableFuture<Message> message;

  private Optional<Disposable> removeSub = Optional.empty();
  private Optional<Disposable> addSub = Optional.empty();
  private Optional<Disposable> updateSub = Optional.empty();

  public MusicPanel(MessageChannel channel, GuildManager manager) {
    this.channel = channel;
    this.manager = manager;
    Snowflake guildId = manager.getGuild().getId();
    if (musicpanels.containsKey(guildId)) {
      musicpanels.get(guildId).shutdown();
    }
    musicpanels.put(guildId, this);
  }

  private Map<ReactionEmoji, Consumer<GuildMusicManager>> createControls() {
    Map<ReactionEmoji, Consumer<GuildMusicManager>> map = new LinkedHashMap<>();
    map.put(ARROW_BACK, musicManager -> musicManager.getScheduler().previous());
    map.put(PAUSE_PLAY, musicManager -> musicManager.getScheduler().togglePause());
    map.put(ARROW_FORWARD, musicManager -> musicManager.getScheduler().next());
    map.put(VOLUME_DOWN, musicManager -> musicManager.getScheduler().volumeDown());
    map.put(VOLUME_UP, musicManager -> musicManager.getScheduler().volumeUp());
    map.put(REPEATE_LIST_EMOJI, toggleRepeatList());
    map.put(REPEATE_SONG_EMOJI, toggleRepeatSong());
    map.put(SHUFFLE_EMOJI, toggleShuffle());
    map.put(REMOVE, musicManager -> musicManager.getScheduler().removeCurrentSong());
    return map;
  }

  private Consumer<GuildMusicManager> toggleShuffle() {
    return musicManager -> {
      TrackScheduler scheduler = musicManager.getScheduler();
      scheduler.setShuffel(!scheduler.isShuffel());
    };
  }

  private Consumer<GuildMusicManager> toggleRepeatSong() {
    return musicManager -> {
      TrackScheduler scheduler = musicManager.getScheduler();
      scheduler.setRepeat(scheduler.getRepeat().equals(Repeat.SONG) ? Repeat.NONE : Repeat.SONG);
    };
  }

  private Consumer<GuildMusicManager> toggleRepeatList() {
    return musicManager -> {
      TrackScheduler scheduler = musicManager.getScheduler();
      scheduler.setRepeat(scheduler.getRepeat().equals(Repeat.LIST) ? Repeat.NONE : Repeat.LIST);
    };
  }

  public void show() {
    message = channel.createMessage(spec -> spec.setEmbed(build())).doOnSuccess(message -> {
      for (ReactionEmoji reaction : controls.keySet()) {
        message.addReaction(reaction).subscribe();
      }
    }).toFuture();
    addSub = Optional
        .of(manager.on(ReactionAddEvent.class).subscribe(reactionEvent -> reactionRecieved(reactionEvent.getMessageId(),
            reactionEvent.getUserId(), reactionEvent.getEmoji())));
    removeSub = Optional.of(
        manager.on(ReactionRemoveEvent.class).subscribe(reactionEvent -> reactionRecieved(reactionEvent.getMessageId(),
            reactionEvent.getUserId(), reactionEvent.getEmoji())));
    manager.getGuildMusicManager().getScheduler().addListener(this);
    updateSub = Optional.of(Flux.interval(Duration.ofSeconds(5), Duration.ofSeconds(5)).subscribe(v -> update()));
  }

  private void reactionRecieved(Snowflake messageId, Snowflake userId, ReactionEmoji emoji) {
    message.thenAccept(message -> {
      if (message.getId().equals(messageId) && !userId.equals(manager.getClient().getSelfId().get())) {
        Consumer<GuildMusicManager> comand = controls.get(emoji);
        if (comand != null) {
          comand.accept(manager.getGuildMusicManager());
        }
      }
    });
  }

  private Consumer<? super EmbedCreateSpec> build() {
    return embed -> {
      embed.setTitle(TITLE);
      TrackScheduler scheduler = manager.getGuildMusicManager().getScheduler();
      Audio currentTrack = scheduler.getCurrentTrack();
      if (currentTrack != null) {
        AudioTrackInfo info = currentTrack.getInfo();
        embed.addField("Title", (scheduler.isPaused() ? PAUSED : PLAYING) + " " + info.title, false);
        embed.addField("Author", info.author, false);
        embed.addField("URL", info.uri, false);
        embed.addField("Duration", DurationFormatUtils.formatDuration(currentTrack.getPosition(), "mm:ss") + "/"
            + DurationFormatUtils.formatDuration(currentTrack.getDuration(), "mm:ss"), false);
      }
      embed.addField("Songs in queue", scheduler.getAmountOfSongsPreviously() + " played previously | "
          + scheduler.getAmountOfSongsInQueue() + " in Queue", false);
      embed.addField("Volume", scheduler.getVolume() + "%", false);
      embed.addField("Status", getShuffle(scheduler), false);
    };
  }

  private String getShuffle(TrackScheduler scheduler) {
    String status = getRepeat(scheduler.getRepeat());
    if (scheduler.isShuffel()) {
      status += SHUFFLE;
    }
    if (status.trim().isEmpty()) {
      status = "None";
    }
    return status;
  }

  private String getRepeat(Repeat repeat) {
    String repeateContent;
    switch (repeat) {
    case LIST:
      repeateContent = REPEATE_LIST;
      break;
    case SONG:
      repeateContent = REPEATE_SONG;
      break;
    default:
      repeateContent = "";
      break;
    }
    return repeateContent;
  }

  @Override
  public void update() {
    message.thenAccept(message -> message.edit(spec -> spec.setEmbed(build())).subscribe());
  }

  private void shutdown() {
    manager.getGuildMusicManager().getScheduler().removeListener(this);
    addSub.ifPresent(Disposable::dispose);
    removeSub.ifPresent(Disposable::dispose);
    updateSub.ifPresent(Disposable::dispose);
    message.thenAccept(message -> message.delete().subscribe());
  }
}
