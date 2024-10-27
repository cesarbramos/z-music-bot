package com.goat.z_music.commands;

import com.goat.z_music.client.DeezerClient;
import com.goat.z_music.dto.GenericData;
import com.goat.z_music.dto.SongDTO;
import com.goat.z_music.enums.PlayOptionsEnum;
import com.goat.z_music.enums.UrlSourceEnum;
import com.goat.z_music.utils.AudioLoader;
import com.goat.z_music.utils.EmbedUtil;
import com.goat.z_music.utils.PlayerCommand;
import com.goat.z_music.utils.TrackUtil;
import dev.arbjerg.lavalink.client.player.LavalinkLoadResult;
import dev.arbjerg.lavalink.client.player.TrackLoaded;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.events.interaction.command.CommandAutoCompleteInteractionEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.Command;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;
import net.dv8tion.jda.api.requests.restaction.interactions.AutoCompleteCallbackAction;
import net.dv8tion.jda.api.requests.restaction.interactions.ReplyCallbackAction;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

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
        SongDTO song = null;

        var sourceEnum = UrlSourceEnum.fromString(query);
        switch (sourceEnum) {
            case null -> {
                var data = deezerClient.search(query);
                if (!data.isEmpty())
                    song = data.getData().getFirst();
            }
            case DEEZER -> {
                var matcher = sourceEnum.getMatcher(query);
                matcher.find();
                Long id = Long.valueOf(matcher.group(1));
                song = deezerClient.findById(id);
            }
            case YOUTUBE -> {
                final var mngr = this.getOrCreateMusicManager(e);

                Mono<SongDTO> songDTOMono = Mono.create(sink ->
                        link.loadItem(query).subscribe(new AudioLoader(e, mngr, null, sink)));

                song = songDTOMono.block();

                var msg = EmbedUtil.playSong(song);
                return e.replyEmbeds(msg);
            }
        }

        if (song == null)
            return e.reply("Canción no encontrada").setEphemeral(true);

        String search = "ytsearch:"+ String.format("%s - %s", song.getTitle(), song.getArtist().getName());

        var msg = EmbedUtil.playSong(song);
        final var mngr = this.getOrCreateMusicManager(e);
        link.loadItem(search).subscribe(new AudioLoader(e, mngr, song));
        return e.replyEmbeds(msg);
    }

    @Override
    public AutoCompleteCallbackAction autocomplete(CommandAutoCompleteInteractionEvent e) {
        var songName = e.getOption(PlayOptionsEnum.SONG.getName()).getAsString();

        var enumx = UrlSourceEnum.fromString(songName);
        if (enumx != null)
            return e.replyChoices(new ArrayList<>());


        var songArtist = e.getOption(PlayOptionsEnum.ARTIST.getName());
        GenericData<SongDTO> results = songArtist == null
                ? deezerClient.search(songName)
                : deezerClient.search(songName, songArtist.getAsString());

        if (results.isEmpty())
            results = deezerClient.search(songName.isEmpty() ? songName : songName.substring(0, 1));

        List<Command.Choice> choices = results.getData().stream()
                .map(x -> {
                    var name = String.format("%s - %s", x.getTitle(), x.getArtist().getName());
                    return new Command.Choice(name, x.getLink());
                })
                .toList();
        return e.replyChoices(choices);
    }

    @Override
    public SlashCommandData definition() {
        var commandData = Commands.slash("play", "Reproduce una canción");

        for (var enu : PlayOptionsEnum.values()) {
            commandData = commandData.addOption(enu.getOptionType(), enu.getName(), enu.getDescription(),
                    enu.isRequired(), enu.isAutocomplete());
        }

        return commandData;
    }
}
