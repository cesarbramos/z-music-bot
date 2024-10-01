package com.goat.z_music.commands;

import com.goat.z_music.dto.SongDTO;
import com.goat.z_music.utils.EmbedUtil;
import com.goat.z_music.utils.PlayerCommand;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.requests.restaction.interactions.ReplyCallbackAction;
import org.springframework.stereotype.Component;

@Component
public class List extends PlayerCommand {
    public ReplyCallbackAction exec(SlashCommandInteractionEvent e) {
        var mgr = getOrCreateMusicManager(e);
        var list = mgr.getAllTracks().stream()
                .map(track -> {
                    var data = track.getUserData(SongDTO.class);
                    return String.format("%s - %s\n", data.getTitle(), data.getArtist().getName());
                })
                .toList();

        MessageEmbed embed = EmbedUtil.songList(list);
        return e.replyEmbeds(embed);
    }
}
