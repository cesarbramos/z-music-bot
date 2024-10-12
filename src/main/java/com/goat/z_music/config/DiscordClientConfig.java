package com.goat.z_music.config;

import com.goat.z_music.config.listeners.ButtonEventListener;
import com.goat.z_music.utils.GuildManagerProviderService;
import com.goat.z_music.utils.ListenerCommandAdapter;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.source.http.HttpAudioSourceManager;
import dev.arbjerg.lavalink.client.Helpers;
import dev.arbjerg.lavalink.client.LavalinkClient;
import dev.arbjerg.lavalink.client.NodeOptions;
import dev.arbjerg.lavalink.client.event.TrackStartEvent;
import dev.arbjerg.lavalink.libraries.jda.JDAVoiceUpdateListener;
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

    private final LavalinkClient lavalinkClient;
    private final GuildManagerProviderService guildManagerProviderService;
    private final ButtonEventListener buttonEventListener;
    private final static String ENV_PROPERTY = "Z_MUSIC_TOKEN";
    private final ListenerCommandAdapter listenerCommandAdapter;

    @Autowired
    public DiscordClientConfig(GuildManagerProviderService guildManagerProviderService,
                               ButtonEventListener buttonEventListener,
                               ListenerCommandAdapter listenerCommandAdapter) throws Exception {
        this.guildManagerProviderService = guildManagerProviderService;
        this.buttonEventListener = buttonEventListener;
        this.listenerCommandAdapter= listenerCommandAdapter;

        var token = System.getenv(ENV_PROPERTY);

        if (token == null)
            throw new Exception("Token not provided : "+ ENV_PROPERTY);

        var client = new LavalinkClient(Helpers.getUserIdFromToken(token));

        var node = new NodeOptions.Builder()
                .setName("principal")
                .setServerUri("ws://localhost:2333")
                .setPassword("youshallnotpass")
                .build();

        client.addNode(node);

        client.on(TrackStartEvent.class)
                .subscribe(track ->
                    log.info("{}: track started: \"{}\"",
                            track.getTrack().getInfo().getIdentifier(),
                            track.getTrack().getInfo().getTitle())
                );

        this.lavalinkClient = client;
    }

    @Bean
    public JDA getDiscordClient() {

        var token = System.getenv(ENV_PROPERTY);

        var client = getLavalinkClient();

        var jda = JDABuilder.createDefault(token, EnumSet.of(
                GatewayIntent.GUILD_MESSAGES,
                GatewayIntent.GUILD_VOICE_STATES)
                )
                .setVoiceDispatchInterceptor(new JDAVoiceUpdateListener(client))
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

        return jda;
    }

    @Bean
    public AudioPlayerManager getAudioPlayerManager() {
        AudioPlayerManager playerManager = new DefaultAudioPlayerManager();
        AudioSourceManagers.registerRemoteSources(playerManager);
        playerManager.registerSourceManager(new HttpAudioSourceManager());
        return playerManager;
    }

    @Bean
    public AudioPlayer getPlayerManager() {
        var playerManager = getAudioPlayerManager();
        return playerManager.createPlayer();
    }

    @Bean
    public LavalinkClient getLavalinkClient() {
        return this.lavalinkClient;
    }

}
