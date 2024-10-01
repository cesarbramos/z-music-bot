package com.goat.z_music.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

@Getter
@Setter
public class SongDTO implements Serializable {

    private Long id;
    private String title;
    private ArtistDTO artist;
    private String link;
    private AlbumDTO album;
    private Long duration;

    @Serial
    private static final long serialVersionUID = -7164679822309967772L;
}
