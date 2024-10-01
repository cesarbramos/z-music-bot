package com.goat.z_music.commands;

import com.goat.z_music.client.DeezerClient;
import com.goat.z_music.utils.*;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.requests.restaction.interactions.ReplyCallbackAction;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@AllArgsConstructor
public class Play extends PlayerCommand {

    private final DeezerClient deezerClient;

    public ReplyCallbackAction exec(SlashCommandInteractionEvent e) {

        var link = lavalinkClient.getOrCreateLink(Long.parseLong(e.getGuild().getId()));

        var memberVoice = e.getMember().getVoiceState();

        if (memberVoice.inAudioChannel()) {
            e.getJDA().getDirectAudioController().connect(memberVoice.getChannel());
        }

        var query = e.getOption("song").getAsString();

        var data = deezerClient.search(query);

        if (data.isEmpty())
            return e.reply("Canción no encontrada").setEphemeral(true);

        var firstSong = data.getData().getFirst();

        String search = "ytsearch:"+ String.format("%s - %s", firstSong.getTitle(), firstSong.getArtist().getName());

        var msg = EmbedUtil.playSong(firstSong);
        final var mngr = this.getOrCreateMusicManager(e);
        link.loadItem(search).subscribe(new AudioLoader(e, mngr, firstSong));
        return e.replyEmbeds(msg);
    }

}
