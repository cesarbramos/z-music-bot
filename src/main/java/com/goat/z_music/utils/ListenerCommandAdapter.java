package com.goat.z_music.utils;

import com.goat.z_music.enums.CommandEnum;
import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.api.events.interaction.command.CommandAutoCompleteInteractionEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.CommandInteractionPayload;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ListenerCommandAdapter extends ListenerAdapter {

    private final ApplicationContext context;

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        BaseCommand cmd = getBean(event);
        var reply = cmd.exec(event);
        if (reply == null)
            reply = event.reply("Sin respuesta!").setEphemeral(true);

        reply.queue();
    }

    @Override
    public void onCommandAutoCompleteInteraction(@NotNull CommandAutoCompleteInteractionEvent event) {
        BaseCommand cmd = getBean(event);
        var reply = cmd.autocomplete(event);
        if (reply == null)
            super.onCommandAutoCompleteInteraction(event);
        else
            reply.queue();
    }

    private BaseCommand getBean(CommandInteractionPayload event) {
        CommandEnum cmdEnum = CommandEnum.fromKeyword(event.getFullCommandName());
        return context.getBean(cmdEnum.getClazz());
    }

}
