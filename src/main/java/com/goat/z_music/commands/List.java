package com.goat.z_music.commands;

import com.goat.z_music.dto.SongDTO;
import com.goat.z_music.utils.PlayerCommand;
import com.goat.z_music.utils.Util;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.requests.restaction.interactions.ReplyCallbackAction;
import org.springframework.stereotype.Component;

@Component
public class List extends PlayerCommand {
    public ReplyCallbackAction exec(SlashCommandInteractionEvent e) {
        var mgr = getOrCreateMusicManager(e);

        var list = mgr.getAllTracks();

        EmbedBuilder mb = new EmbedBuilder()
                .setTitle("Cola de canciones");
        StringBuilder sb = new StringBuilder();

        //StringSelectMenu.Builder menu = StringSelectMenu.create("Canción")
        //        .setPlaceholder("Selecciona una canción");

        for (int i = 0; list.size() > i; i++) {
            var track = list.get(i);
            var data = track.getUserData(SongDTO.class);
            var durationStr = String.format("(%s) ", Util.formatDuration(data.getDuration()));
            var fieldData = String.format("%d. %s%s - %s\n", i + 1, durationStr,
                    data.getTitle(),
                    data.getArtist().getName());
            sb.append(fieldData);
            /*
            menu.addOption(data.getTitle(), i + "",
                    durationStr + data.getArtist().getName(),
                    Util.fromPosition(i + 1)); */
        }
        mb.setDescription(Util.inlineCode(sb.toString()));

        //MessageEmbed embed = EmbedUtil.songList(list);
        return e.replyEmbeds(mb.build());
    }
}
