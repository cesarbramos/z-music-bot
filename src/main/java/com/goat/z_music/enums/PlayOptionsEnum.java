package com.goat.z_music.enums;

import lombok.Getter;

@Getter
public enum PlayOptionsEnum {
    SONG("song"),
    ARTIST("artist");

    private final String name;

    PlayOptionsEnum(String name) {
        this.name = name;
    }

}
