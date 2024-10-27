package com.goat.z_music.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Getter
@RequiredArgsConstructor
public enum UrlSourceEnum {
    DEEZER("^https://www.deezer.com/track/(\\d+)/?"),
    YOUTUBE("^((https:\\/\\/)?(www\\.)?youtube\\.com\\/watch\\?v=.+\\/?)|((https:\\/\\/)?youtu\\.be\\/.+)");

    private final Pattern pattern;

    UrlSourceEnum(String regex) {
        pattern = Pattern.compile(regex);
    }

    public static UrlSourceEnum fromString(String str) {
        for (UrlSourceEnum urlSourceEnum : UrlSourceEnum.values()) {
            if (urlSourceEnum.getPattern().matcher(str).matches())
                return urlSourceEnum;
        }
        return null;
    }

    public Matcher getMatcher(String test) {
        return pattern.matcher(test);
    }

}
