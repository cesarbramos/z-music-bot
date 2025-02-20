package com.goat.z_music.commands;

import com.goat.z_music.config.RadioConfig;
import com.goat.z_music.dto.ArtistDTO;
import com.goat.z_music.dto.SongDTO;
import com.goat.z_music.enums.RadiosEnum;
import com.goat.z_music.utils.PlayerCommand;
import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import lombok.AllArgsConstructor;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.interaction.command.CommandAutoCompleteInteractionEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.Command;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;
import net.dv8tion.jda.api.requests.restaction.interactions.AutoCompleteCallbackAction;
import net.dv8tion.jda.api.requests.restaction.interactions.ReplyCallbackAction;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Stream;

@Service
@AllArgsConstructor
public class Radio extends PlayerCommand {
    private static final String OPT_EMISORA = "emisora";
    private final RadioConfig radioConfig;
    private static final List<Command.Choice> CHOICES = Stream.of(RadiosEnum.values())
            .map(x -> new Command.Choice(x.getTitle(), x.getCode()))
            .toList();

    @Override
    public ReplyCallbackAction exec(SlashCommandInteractionEvent e) {
        var memberVoice = e.getMember().getVoiceState();

        if (memberVoice.inAudioChannel()) {
            e.getJDA().getDirectAudioController().connect(memberVoice.getChannel());
        }

        OptionMapping emisoraOpt = e.getOption(OPT_EMISORA);
        if (emisoraOpt == null)
            return e.reply("Emisora no válida");

        String url = radioConfig.getRadioUrl() + emisoraOpt.getAsString() + radioConfig.getRadioExtension();

        RadiosEnum radioEnum = RadiosEnum.fromCode(emisoraOpt.getAsString());
        if (radioEnum == null)
            return e.reply("Emisora no válida");

        playStream(e.getGuild(), url, e, radioEnum);

        return e.reply("Reproduciendo " + radioEnum.getTitle());
    }

    @Override
    public AutoCompleteCallbackAction autocomplete(CommandAutoCompleteInteractionEvent e) {
        return e.replyChoices(CHOICES);
    }

    public void playStream(Guild guild, String url, SlashCommandInteractionEvent e, RadiosEnum radioEnum) {
        final var mngr = this.getOrCreateMusicManager(e);

        audioPlayerManager.loadItem(url, new AudioLoadResultHandler() {

            @Override
            public void trackLoaded(AudioTrack result) {
                var data = new SongDTO();
                data.setDuration(-1L);
                data.setTitle(radioEnum.getTitle());
                var artist = new ArtistDTO();
                artist.setName("emisora");
                data.setArtist(artist);

                result.setUserData(data);
                mngr.scheduler.queue(result);
            }

            @Override
            public void playlistLoaded(AudioPlaylist playlist) {

            }

            @Override
            public void noMatches() {

            }

            @Override
            public void loadFailed(@NotNull FriendlyException ex) {
                e.getHook()
                        .sendMessage("Radio failed loaded")
                        .queue();
            }
        });
    }

    @Override
    public SlashCommandData definition() {
        return Commands.slash("radio", "Reproduce una radio seleccionada")
                .addOption(OptionType.STRING, OPT_EMISORA, "Emisora colombiana", true, true);
    }
}
