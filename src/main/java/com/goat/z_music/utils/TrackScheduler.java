package com.goat.z_music.utils;

import dev.arbjerg.lavalink.protocol.v4.Message;
import dev. arbjerg.lavalink.client.player.Track;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class TrackScheduler {
    private final GuildMusicManager guildMusicManager;
    public final Queue<Track> queue = new LinkedList<>();
    public final Queue<Track> removedTracks = new LinkedList<>();

    public TrackScheduler(GuildMusicManager guildMusicManager) {
        this.guildMusicManager = guildMusicManager;
    }

    public void enqueue(Track track) {
        var player = guildMusicManager.getPlayer();
        if (player.isEmpty()) {
            this.guildMusicManager.getLink().ifPresent((link) ->{
                this.queue.offer(track);
                link.createOrUpdatePlayer()
                        .setTrack(poll())
                        .setVolume(80)
                        .subscribe();
            });
            return;
        }

        if (player.get().getTrack() == null) {
            this.queue.offer(track);
            this.startTrack(poll());
        } else {
            this.queue.offer(track);
        }
    }

    public void enqueuePlaylist(List<Track> tracks) {
        this.queue.addAll(tracks);
        this.guildMusicManager.getPlayer().ifPresentOrElse(
                (player) -> {
                    if (player.getTrack() == null) {
                        this.startTrack(poll());
                    }
                },
                () -> {
                    this.startTrack(poll());
                }
        );
    }

    public void nextTrack() {
        this.startTrack(poll());
    }

    public void onTrackStart(Track track) {
        // Your homework: Send a message to the channel somehow, have fun!
        System.out.println("Track started: " + track.getInfo().getTitle());
    }

    public void onTrackEnd(Track lastTrack, Message.EmittedEvent.TrackEndEvent.AudioTrackEndReason endReason) {
        if (endReason.getMayStartNext()) {
            final var nextTrack = poll();

            if (nextTrack != null) {
                this.startTrack(nextTrack);
            }
        }
    }

    private void startTrack(Track track) {
        this.guildMusicManager.getLink().ifPresent(
                (link) -> link.createOrUpdatePlayer()
                        .setTrack(track)
                        .setVolume(80)
                        .subscribe()
        );
    }

    private Track poll() {
        var lastElement = this.queue.poll();
        removedTracks.offer(lastElement);
        return lastElement;
    }

}