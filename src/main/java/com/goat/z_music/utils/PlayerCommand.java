package com.goat.z_music.utils;

import dev.arbjerg.lavalink.client.LavalinkClient;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public abstract class PlayerCommand extends BaseCommand {

    @Autowired
    protected LavalinkClient lavalinkClient;

    @Autowired
    private GuildManagerProviderService guildManagerProviderService;

    private GuildMusicManager getOrCreateMusicManager(long guildId) {
        var mng = this.guildManagerProviderService.get(guildId);

        if (mng == null) {
            mng = new GuildMusicManager(guildId, lavalinkClient);
            this.guildManagerProviderService.put(guildId, mng);
        }

        return mng;
    }

    protected GuildMusicManager getOrCreateMusicManager(SlashCommandInteractionEvent e) {
        return getOrCreateMusicManager(getGuildId(e));
    }

}
