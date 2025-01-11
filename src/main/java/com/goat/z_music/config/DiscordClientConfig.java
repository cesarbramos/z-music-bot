package com.goat.z_music.config;

import com.github.topi314.lavasrc.deezer.DeezerAudioSourceManager;
import com.github.topi314.lavasrc.deezer.DeezerAudioTrack;
import com.goat.z_music.config.listeners.ButtonEventListener;
import com.goat.z_music.utils.GuildManagerProviderService;
import com.goat.z_music.utils.ListenerCommandAdapter;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.source.http.HttpAudioSourceManager;
import dev.lavalink.youtube.YoutubeAudioSourceManager;
import dev.lavalink.youtube.clients.MWeb;
import dev.lavalink.youtube.clients.Music;
import dev.lavalink.youtube.clients.Tv;
import dev.lavalink.youtube.clients.Web;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceUpdateEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.GatewayIntent;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.EnumSet;

@Slf4j
@Configuration
public class DiscordClientConfig {

    private final GuildManagerProviderService guildManagerProviderService;
    private final ButtonEventListener buttonEventListener;
    private final static String ENV_PROPERTY = "Z_MUSIC_TOKEN";
    private final ListenerCommandAdapter listenerCommandAdapter;
    private final RegisterCommands registerCommands;

    private final DeezerConfig deezerConfig;

    @Autowired
    public DiscordClientConfig(GuildManagerProviderService guildManagerProviderService,
                               ButtonEventListener buttonEventListener,
                               DeezerConfig deezerConfig,
                               ListenerCommandAdapter listenerCommandAdapter,
                               RegisterCommands registerCommands) throws Exception {
        this.guildManagerProviderService = guildManagerProviderService;
        this.buttonEventListener = buttonEventListener;
        this.listenerCommandAdapter= listenerCommandAdapter;
        this.registerCommands = registerCommands;
        this.deezerConfig = deezerConfig;

        var token = System.getenv(ENV_PROPERTY);

        if (token == null)
            throw new Exception("Token not provided : "+ ENV_PROPERTY);
    }

    @Bean
    public JDA getDiscordClient() {

        var token = System.getenv(ENV_PROPERTY);

        var jda = JDABuilder.createDefault(token, EnumSet.of(
                GatewayIntent.GUILD_MESSAGES,
                GatewayIntent.GUILD_VOICE_STATES)
                )
                .build();

        jda.addEventListener(new ListenerAdapter() {
            @Override
            public void onGuildVoiceUpdate(@NotNull GuildVoiceUpdateEvent event) {
                boolean isMe = event.getEntity().getId().equals(event.getJDA().getSelfUser().getId());
                if (isMe && !event.getVoiceState().inAudioChannel())
                    guildManagerProviderService.remove(event.getGuild().getIdLong()).clearQueue();

            }
        });

        jda.addEventListener(buttonEventListener);
        jda.addEventListener(listenerCommandAdapter);

        registerCommands.register(jda);

        return jda;
    }

    @Bean
    public AudioPlayerManager getAudioPlayerManager() {
        AudioPlayerManager playerManager = new DefaultAudioPlayerManager();

        YoutubeAudioSourceManager youtube = new YoutubeAudioSourceManager( true, new Music(), new Web(), new MWeb(), new Tv());
        DeezerAudioSourceManager deezer = new DeezerAudioSourceManager(deezerConfig.getMasterKey(), "");

        playerManager.registerSourceManager(deezer);
        playerManager.registerSourceManager(youtube);
        AudioSourceManagers.registerRemoteSources(playerManager);
        AudioSourceManagers.registerLocalSource(playerManager);
        deezer.setFormats(new DeezerAudioTrack.TrackFormat[] { DeezerAudioTrack.TrackFormat.MP3_320, DeezerAudioTrack.TrackFormat.MP3_256, DeezerAudioTrack.TrackFormat.MP3_128 });
        playerManager.registerSourceManager(new HttpAudioSourceManager());
        return playerManager;
    }

}
