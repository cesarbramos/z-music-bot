package com.goat.z_music.enums;

import com.goat.z_music.commands.*;
import com.goat.z_music.utils.BaseCommand;
import lombok.Getter;

@Getter
public enum CommandEnum {
    PLAY("play", Play.class),
    PAUSE("pause", Pause.class),
    RESUME("resume", Resume.class),
    SKIP("skip", Skip.class),
    LIST("list", List.class),
    JOIN("join", Join.class),
    NO_COMMAND(null, NoCommand.class);

    private final String keyword;
    private final Class<? extends BaseCommand> clazz;

    <T extends BaseCommand> CommandEnum(String keyword, Class<T> clazz) {
       this.keyword = keyword;
       this.clazz = clazz;
    }

    public static CommandEnum fromKeyword(String keyword) {
        for (CommandEnum cmd : values()) {
            if (cmd.getKeyword().equals(keyword))
                return cmd;
        }
        return NO_COMMAND;
    }

}
