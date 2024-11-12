package com.goat.z_music.utils;

import net.dv8tion.jda.api.events.interaction.command.CommandAutoCompleteInteractionEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;
import net.dv8tion.jda.api.requests.restaction.interactions.AutoCompleteCallbackAction;
import net.dv8tion.jda.api.requests.restaction.interactions.ReplyCallbackAction;
import org.springframework.stereotype.Service;

@Service
public abstract class BaseCommand {

    public abstract ReplyCallbackAction exec(SlashCommandInteractionEvent e);

    public AutoCompleteCallbackAction autocomplete(CommandAutoCompleteInteractionEvent e) {
        return null;
    }

    protected Long getGuildId(SlashCommandInteractionEvent e) {
        if (e.getGuild() == null)
            return null;
        return e.getGuild().getIdLong();
    }

    public abstract SlashCommandData definition();

}
