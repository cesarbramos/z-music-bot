package com.goat.z_music.utils;

import com.goat.z_music.dto.AlbumDTO;
import com.goat.z_music.dto.ArtistDTO;
import com.goat.z_music.dto.SongDTO;
import com.goat.z_music.enums.UrlSourceEnum;
import dev.arbjerg.lavalink.client.player.TrackLoaded;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TrackUtil {

    public static SongDTO getTrackLoaded(TrackLoaded result) {
        SongDTO song = new SongDTO();
        var info = result.getTrack().getInfo();
        song.setDuration(info.getLength());
        var artist = new ArtistDTO();
        artist.setName(info.getAuthor());
        song.setArtist(artist);
        var album = new AlbumDTO();
        album.setCover(info.getArtworkUrl());
        album.setTitle("");
        song.setAlbum(album);
        song.setTitle(info.getTitle());
        song.setLink(info.getUri());
        return song;
    }

}
