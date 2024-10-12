package com.goat.z_music.utils;

import net.dv8tion.jda.api.entities.emoji.Emoji;

public class Util {

    public static int SEGUNDOS = 60;

    public static String formatDuration(long duration) {
        long min = (long) Math.floor((double) duration / SEGUNDOS);
        long minsec = min * SEGUNDOS;
        long seconds = duration - minsec;
        return String.format("%02d:%02d", min, seconds);
    }

    public static String inlineCode(String content) {
        return "`"+content+"`";
    }

    public static Emoji fromPosition(int position) {
        if (position > 10 || position < 1) return null;
        if (position == 10) return Emoji.fromUnicode("🔟");
        var unicode = String.format("U+003%d U+20E3", position);
        return Emoji.fromUnicode(unicode);
    }

}
