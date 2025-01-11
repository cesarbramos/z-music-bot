package com.goat.z_music.utils;

import com.goat.z_music.dto.SongDTO;
import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.jetbrains.annotations.NotNull;
import reactor.core.publisher.MonoSink;

public class AudioLoader implements AudioLoadResultHandler {
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
    public void trackLoaded(@NotNull AudioTrack result) {
        if (songMonoSink != null) {
            song = TrackUtil.getTrackLoaded(result);
            songMonoSink.success(song);
        }
        result.setUserData(song);
        this.mngr.scheduler.queue(result);
        //event.getHook().sendMessage("Added to queue: " + trackTitle + "\nRequested by: <@" + event.getUser().getName() + '>').queue();
    }

    @Override
    public void playlistLoaded(@NotNull AudioPlaylist result) {
        final int trackCount = result.getTracks().size();
       // event.getHook()
        //        .sendMessage("Added " + trackCount + " tracks to the queue from " + result.getInfo().getName() + "!")
        //        .queue();

        this.mngr.scheduler.queuePlaylist(result);
    }

    @Override
    public void noMatches() {
        //event.getHook().sendMessage("No matches found for your input!").queue();
    }

    @Override
    public void loadFailed(FriendlyException ex) {
        event.getHook().sendMessage("Failed to load track! " + ex.getMessage()).queue();
    }
}