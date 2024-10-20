package com.goat.z_music.config;

import com.goat.z_music.utils.BaseCommand;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class RegisterCommands {

    private final ApplicationContext applicationContext;

    public void register(JDA jda) {
        List<CommandData> commands = new ArrayList<>();
        log.info("Registering commands");

        for (var cmd : applicationContext.getBeansOfType(BaseCommand.class).values()) {
            var def = cmd.definition();
            log.info("Registering command {}", def.getName());
            commands.add(def);
        }

        jda.updateCommands()
                .addCommands(commands)
                .queue();

        log.info("Registered commands");
    }

}
