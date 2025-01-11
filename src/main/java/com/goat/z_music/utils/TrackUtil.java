package com.goat.z_music.utils;

import com.goat.z_music.dto.AlbumDTO;
import com.goat.z_music.dto.ArtistDTO;
import com.goat.z_music.dto.SongDTO;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackInfo;
import dev.lavalink.youtube.track.YoutubeAudioTrack;

public class TrackUtil {

    public static SongDTO getTrackLoaded(AudioTrack track) {
        var result = (YoutubeAudioTrack) track;
        SongDTO song = new SongDTO();
        AudioTrackInfo info = result.getInfo();
        song.setDuration(result.getDuration());
        var artist = new ArtistDTO();
        artist.setName(result.getInfo().author);
        song.setArtist(artist);
        var album = new AlbumDTO();
        album.setCover(info.artworkUrl);
        album.setTitle("");
        song.setAlbum(album);
        song.setTitle(info.title);
        song.setLink(info.uri);
        return song;
    }

}
