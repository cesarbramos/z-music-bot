package com.goat.z_music.utils;

import com.goat.z_music.dto.SongDTO;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.interactions.components.buttons.Button;

import java.awt.*;
import java.util.Arrays;
import java.util.List;

public class EmbedUtil {

    public static MessageEmbed playSong(SongDTO song) {
        return playSong(song.getTitle(), song.getArtist().getName(),
                song.getArtist().getPicture(), song.getAlbum().getTitle(),
                Util.formatDuration(song.getDuration()), song.getLink(), song.getAlbum().getCover());
    }

    public static MessageEmbed playSong(String title, String author, String authorImg, String album, String duration, String url, String thumbnail) {
        return new EmbedBuilder()
                .setColor(Color.decode("#A238FF"))
                .setTitle(title)
                .setAuthor(author, null, authorImg)
                .addField("Álbum", Util.inlineCode(album), true)
                .addField("Duración", Util.inlineCode(duration), true)
                .setUrl(url)
                .setThumbnail(thumbnail)
                .build();
    }

    public static MessageEmbed songList(List<String> songs) {
        var sb = new StringBuilder();
        for (String song : songs) {
            sb.append(song).append("\n");
        }
        return new EmbedBuilder()
                .setTitle("Cola de canciones")
                .setColor(Color.DARK_GRAY)
                .setDescription(sb.toString())
                .build();
    }

    public static List<Button> getActionButtons() {
        return Arrays.asList(
                Button.secondary("page_first", Emoji.fromUnicode("⏪")),
                Button.secondary("page_back", Emoji.fromUnicode("◀")),
                Button.secondary("page_cancel", Emoji.fromUnicode("❌")),
                Button.secondary("page_next", Emoji.fromUnicode("▶")),
                Button.secondary("page_last", Emoji.fromUnicode("⏩"))
        );
    }


}
