package com.goat.z_music.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

@Getter
@Setter
public class AlbumDTO implements Serializable {

    private Long id;
    private String title;
    private String cover;

    @Serial
    private static final long serialVersionUID = 2309113738889275965L;
}
