package com.goat.z_music.commands;

import com.goat.z_music.utils.BaseCommand;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;
import net.dv8tion.jda.api.requests.restaction.interactions.ReplyCallbackAction;

public class NoCommand extends BaseCommand {
    @Override
    public ReplyCallbackAction exec(SlashCommandInteractionEvent e) {
        return e.reply("Comando inexistente").setEphemeral(true);
    }

    @Override
    public SlashCommandData definition() {
        return Commands.slash("Sin commando", "Sin commando");
    }
}
