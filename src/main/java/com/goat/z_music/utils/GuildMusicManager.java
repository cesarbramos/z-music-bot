package com.goat.z_music.utils;

import dev.arbjerg.lavalink.client.LavalinkClient;
import dev.arbjerg.lavalink.client.Link;
import dev.arbjerg.lavalink.client.player.LavalinkPlayer;
import dev.arbjerg.lavalink.client.player.Track;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public class GuildMusicManager {
    public final TrackScheduler scheduler = new TrackScheduler(this);
    private final long guildId;
    private final LavalinkClient lavalink;

    public GuildMusicManager(long guildId, LavalinkClient lavalink) {
        this.lavalink = lavalink;
        this.guildId = guildId;
    }

    private void setPaused(boolean state) {
        this.getPlayer().ifPresent(
                (player) -> player.setPaused(state)
                        .subscribe()
        );
    }

    public void pause() {
        setPaused(true);
    }

    public void resume() {
        setPaused(false);
    }

    public void skip() {
        this.scheduler.nextTrack();
    }

    public List<Track> getAllTracks() {
        return Stream.concat(this.scheduler.removedTracks.stream(),
                this.scheduler.queue.stream()).toList();
    }

    public void previous() {
        this.getPlayer().ifPresent((player) -> {
            if (player.getPosition() <= 0)
                return;

            player.setPosition(player.getPosition() + 1)
                    .subscribe();
        });
    }

    public void clearQueue() {
        this.scheduler.queue.clear();
    }

    public Optional<Link> getLink() {
        return Optional.ofNullable(
                this.lavalink.getLinkIfCached(this.guildId)
        );
    }

    public Optional<LavalinkPlayer> getPlayer() {
        return this.getLink().map(Link::getCachedPlayer);
    }
}
