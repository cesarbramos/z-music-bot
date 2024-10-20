package com.goat.z_music.commands;

import com.goat.z_music.client.DeezerClient;
import com.goat.z_music.dto.GenericData;
import com.goat.z_music.dto.SongDTO;
import com.goat.z_music.enums.PlayOptionsEnum;
import com.goat.z_music.utils.AudioLoader;
import com.goat.z_music.utils.EmbedUtil;
import com.goat.z_music.utils.PlayerCommand;
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

import java.util.List;

import static com.goat.z_music.utils.TrackUtil.isDeezerTrack;

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

        var matcher = isDeezerTrack(query);

        if (matcher.find()) {
            Long id = Long.valueOf(matcher.group(1));
            song = deezerClient.findById(id);
        } else {
            var data = deezerClient.search(query);
            if (!data.isEmpty())
                song = data.getData().getFirst();
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
