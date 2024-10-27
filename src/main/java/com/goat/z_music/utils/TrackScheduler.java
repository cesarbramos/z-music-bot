package com.goat.z_music.utils;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;
import dev.arbjerg.lavalink.client.event.TrackEndEvent;
import dev.arbjerg.lavalink.client.player.Track;
import lombok.extern.slf4j.Slf4j;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

@Slf4j
public class TrackScheduler extends AudioEventAdapter {
    private final GuildMusicManager guildMusicManager;
    public final Queue<Track> queue = new LinkedList<>();
    public final Queue<Track> removedTracks = new LinkedList<>();

    public TrackScheduler(GuildMusicManager guildMusicManager) {
        this.guildMusicManager = guildMusicManager;
        this.guildMusicManager.getLavalinkClient()
                .on(TrackEndEvent.class)
                .subscribe((event) -> {
                    if (event.getEndReason().getMayStartNext())
                        nextTrack();
                });
    }

    public void enqueue(Track track) {
        var player = guildMusicManager.getPlayer();
        if (player.isEmpty()) {
            this.guildMusicManager.getLink().ifPresent((link) ->{
                this.queue.offer(track);
                link.createOrUpdatePlayer()
                        .setTrack(poll())
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
                () -> this.startTrack(poll())
        );
    }

    public void nextTrack() {
        this.startTrack(poll());
    }

    public void onTrackStart(Track track) {
        // Your homework: Send a message to the channel somehow, have fun!
        System.out.println("Track started: " + track.getInfo().getTitle());

    }

    @Override
    public void onTrackEnd(AudioPlayer player, AudioTrack track, AudioTrackEndReason endReason) {
        if (endReason.mayStartNext) {
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
                        .subscribe()
        );
    }

    private Track poll() {
        var lastElement = this.queue.poll();
        removedTracks.offer(lastElement);
        return lastElement;
    }

}