package com.goat.z_music.enums;

import lombok.Getter;
import net.dv8tion.jda.api.interactions.commands.OptionType;

import static net.dv8tion.jda.api.interactions.commands.OptionType.STRING;

@Getter
public enum PlayOptionsEnum {
    SONG("song", "Nombre de la canción a reproducir", true, true),
    ARTIST("artist", "Aplica una busqueda incuyendo el artista", false, false);

    private final OptionType optionType;
    private final String name;
    private final String description;
    private final boolean required;
    private final boolean autocomplete;

    PlayOptionsEnum(String name, String description, boolean required, boolean autocomplete) {
        this.optionType = STRING;
        this.name = name;
        this.description = description;
        this.required = required;
        this.autocomplete = autocomplete;
    }

}
