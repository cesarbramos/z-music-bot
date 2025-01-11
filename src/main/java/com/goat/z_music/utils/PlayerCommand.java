package com.goat.z_music.utils;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public abstract class PlayerCommand extends BaseCommand {

    @Autowired
    protected AudioPlayerManager audioPlayerManager;

    @Autowired
    private GuildManagerProviderService guildManagerProviderService;

    private GuildMusicManager getOrCreateMusicManager(Guild guild) {
        long guildId = guild.getIdLong();
        var mng = this.guildManagerProviderService.get(guildId);

        if (mng == null) {
            mng = new GuildMusicManager(this.audioPlayerManager);
            this.guildManagerProviderService.put(guildId, mng);
        }

        guild.getAudioManager().setSendingHandler(mng.getSendHandler());

        return mng;
    }

    protected GuildMusicManager getOrCreateMusicManager(SlashCommandInteractionEvent e) {
        return getOrCreateMusicManager(e.getGuild());
    }

}
