package com.goat.z_music.commands;

import com.goat.z_music.utils.PlayerCommand;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;
import net.dv8tion.jda.api.requests.restaction.interactions.ReplyCallbackAction;
import org.springframework.stereotype.Service;

@Service
public class Pause extends PlayerCommand {
    public ReplyCallbackAction exec(SlashCommandInteractionEvent e) {
        var mgr = getOrCreateMusicManager(e);
        mgr.scheduler.pause();
        return e.reply("Paused");
    }

    @Override
    public SlashCommandData definition() {
        return Commands.slash("pause", "Pausa la canción actual");
    }
}
