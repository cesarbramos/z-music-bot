package com.goat.z_music.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TrackUtil {
    private final static Pattern DEEZER_REGEX_URL = Pattern.compile("^https://www.deezer.com/track/(\\d+)/?");

    public static Matcher isDeezerTrack(String value) {
        return DEEZER_REGEX_URL.matcher(value);
    }

    public String getIdFromUrl(String url) {
        DEEZER_REGEX_URL.matcher(url).find();
        return "";
    }

}
