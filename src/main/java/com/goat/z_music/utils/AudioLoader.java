package com.goat.z_music.utils;

import com.goat.z_music.dto.SongDTO;
import dev.arbjerg.lavalink.client.AbstractAudioLoadResultHandler;
import dev.arbjerg.lavalink.client.player.*;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.jetbrains.annotations.NotNull;
import reactor.core.publisher.MonoSink;

import java.util.List;

public class AudioLoader extends AbstractAudioLoadResultHandler {
    private final SlashCommandInteractionEvent event;
    private final GuildMusicManager mngr;
    private SongDTO song;
    private final MonoSink<SongDTO> songMonoSink;

    public AudioLoader(SlashCommandInteractionEvent event, GuildMusicManager mngr, SongDTO song) {
        this(event, mngr, song, null);
    }

    public AudioLoader(SlashCommandInteractionEvent event, GuildMusicManager mngr, SongDTO song, MonoSink<SongDTO> songMonoSink) {
        this.event = event;
        this.mngr = mngr;
        this.song = song;
        this.songMonoSink = songMonoSink;
    }

    @Override
    public void ontrackLoaded(@NotNull TrackLoaded result) {
        if (songMonoSink != null) {
            song = TrackUtil.getTrackLoaded(result);
            songMonoSink.success(song);
        }
        final Track track = result.getTrack();
        track.setUserData(song);
        this.mngr.scheduler.enqueue(track);
        //event.getHook().sendMessage("Added to queue: " + trackTitle + "\nRequested by: <@" + event.getUser().getName() + '>').queue();
    }

    @Override
    public void onPlaylistLoaded(@NotNull PlaylistLoaded result) {
        final int trackCount = result.getTracks().size();
       // event.getHook()
        //        .sendMessage("Added " + trackCount + " tracks to the queue from " + result.getInfo().getName() + "!")
        //        .queue();

        this.mngr.scheduler.enqueuePlaylist(result.getTracks());
    }

    @Override
    public void onSearchResultLoaded(@NotNull SearchResult result) {
        final List<Track> tracks = result.getTracks();

        if (tracks.isEmpty()) {
            //event.getHook().sendMessage("No tracks found!").queue();
            return;
        }

        final Track firstTrack = tracks.get(0);
        firstTrack.setUserData(song);

        //event.getHook().sendMessage("Adding to queue: " + firstTrack.getInfo().getTitle()).queue();

        this.mngr.scheduler.enqueue(firstTrack);
    }

    @Override
    public void noMatches() {
        //event.getHook().sendMessage("No matches found for your input!").queue();
    }

    @Override
    public void loadFailed(@NotNull LoadFailed result) {
        event.getHook().sendMessage("Failed to load track! " + result.getException().getMessage()).queue();
    }
}