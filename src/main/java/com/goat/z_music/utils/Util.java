package com.goat.z_music.utils;

public class Util {

    public static int SEGUNDOS = 60;

    public static String formatDuration(long duration) {
        long min = (long) Math.floor((double) duration / SEGUNDOS);
        long minsec = min * SEGUNDOS;
        long seconds = duration - minsec;
        return String.format("%02d:%02d", min, seconds);
    }

    public static String inlineCode(String content) {
        return isEmpty(content) ? "" : "`"+content+"`";
    }

    public static boolean isEmpty(String str) {
        return str == null || str.isEmpty();
    }

}
