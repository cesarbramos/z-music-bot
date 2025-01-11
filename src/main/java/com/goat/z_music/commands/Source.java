package com.goat.z_music.commands;

import com.goat.z_music.utils.PlayerCommand;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;
import net.dv8tion.jda.api.requests.restaction.interactions.ReplyCallbackAction;
import org.springframework.stereotype.Component;

@Component
public class Source extends PlayerCommand {
    public ReplyCallbackAction exec(SlashCommandInteractionEvent e) {
        var mgr = getOrCreateMusicManager(e);
        return e.reply("");
    }

    @Override
    public SlashCommandData definition() {
        return Commands.slash("source", "Cambia la fuente del audio");
    }
}
