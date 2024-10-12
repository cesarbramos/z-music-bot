package com.goat.z_music.config.listeners;

import com.goat.z_music.enums.ButtonPageEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ButtonEventListener extends ListenerAdapter {

    @Override
    public void onButtonInteraction(ButtonInteractionEvent event) {
        ButtonPageEnum buttonPageEnum = ButtonPageEnum.fromCode(event.getButton().getId());
        log.info("BUTTON EVENT: {}", event.getButton().getId());
        if (buttonPageEnum == null) return;

        var message = event.getMessage().getEmbeds().get(0);
        var footer = message.getFooter();
        String currPage = footer.getText().split(" ")[1];
    }

}
