package com.goat.z_music.utils;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.requests.restaction.interactions.ReplyCallbackAction;
import org.springframework.stereotype.Service;

@Service
public abstract class BaseCommand {

    public abstract ReplyCallbackAction exec(SlashCommandInteractionEvent e);

    protected long getGuildId(SlashCommandInteractionEvent e) {
        return e.getGuild().getIdLong();
    }

}
