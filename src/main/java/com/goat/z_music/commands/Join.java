package com.goat.z_music.commands;

import com.goat.z_music.utils.PlayerCommand;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;
import net.dv8tion.jda.api.requests.restaction.interactions.ReplyCallbackAction;
import org.springframework.stereotype.Component;

@Component
public class Join extends PlayerCommand {
    public ReplyCallbackAction exec(SlashCommandInteractionEvent e) {
        var memberVoice = e.getMember().getVoiceState();

        if (!memberVoice.inAudioChannel())
            return e.reply("Debes estar en algún canal de voz para usar este comandos");

        e.getJDA().getDirectAudioController().connect(memberVoice.getChannel());

        return e.reply("Joined!");
    }

    @Override
    public SlashCommandData definition() {
        return Commands.slash("join", "Une el bot a el canal de voz donde esté el usuario");
    }
}
