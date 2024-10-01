package com.goat.z_music.config;

import com.goat.z_music.enums.CommandEnum;
import com.goat.z_music.utils.BaseCommand;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.source.http.HttpAudioSourceManager;
import com.sedmelluq.discord.lavaplayer.source.youtube.YoutubeAudioSourceManager;
import dev.arbjerg.lavalink.client.Helpers;
import dev.arbjerg.lavalink.client.LavalinkClient;
import dev.arbjerg.lavalink.client.NodeOptions;
import dev.arbjerg.lavalink.client.event.TrackStartEvent;
import dev.arbjerg.lavalink.client.loadbalancing.VoiceRegion;
import dev.arbjerg.lavalink.libraries.jda.JDAVoiceUpdateListener;
import dev.arbjerg.lavalink.protocol.v4.VoiceState;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.GatewayIntent;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.EnumSet;

@Slf4j
@Configuration
public class DiscordClientConfig {

    private final ApplicationContext context;
    private final LavalinkClient lavalinkClient;

    @Autowired
    public DiscordClientConfig(ApplicationContext context, ApplicationArguments args) {
        this.context = context;

        var token = args.getSourceArgs()[0];
        var client = new LavalinkClient(Helpers.getUserIdFromToken(token));

        var node = new NodeOptions.Builder()
                .setName("principal")
                .setServerUri("ws://localhost:2333")
                .setPassword("youshallnotpass")
                .build();

        client.addNode(node);

        client.on(TrackStartEvent.class)
                .subscribe(track -> {
                    log.info("{}: track started: \"{}\"",
                            track.getTrack().getInfo().getIdentifier(),
                            track.getTrack().getInfo().getTitle());
                });

        this.lavalinkClient = client;
    }

    @Bean
    public JDA getDiscordClient(ApplicationArguments args) {

        var token = args.getSourceArgs()[0];

        var client = getLavalinkClient(args);

        var jda = JDABuilder.createDefault(token, EnumSet.of(
                GatewayIntent.GUILD_MESSAGES,
                GatewayIntent.MESSAGE_CONTENT,
                GatewayIntent.GUILD_VOICE_STATES)
                )
                .setVoiceDispatchInterceptor(new JDAVoiceUpdateListener(client))
                .build();

        jda.addEventListener(new ListenerAdapter() {
            @Override
            public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
                CommandEnum cmdEnum = CommandEnum.fromKeyword(event.getFullCommandName());
                if (cmdEnum == null) {
                    event.reply("Comando inexistente").setEphemeral(true).queue();
                    return;
                }

                BaseCommand cmd = context.getBean(cmdEnum.getClazz());
                var result = cmd.exec(event);
                if (result == null)
                    result = event.reply("Pong!").setEphemeral(true);

                result.queue();
            }
        });

        return jda;
    }

    @Bean
    public AudioPlayerManager getAudioPlayerManager() {
        AudioPlayerManager playerManager = new DefaultAudioPlayerManager();
        AudioSourceManagers.registerRemoteSources(playerManager);
        playerManager.registerSourceManager(new YoutubeAudioSourceManager());
        playerManager.registerSourceManager(new HttpAudioSourceManager());
        AudioSourceManagers.registerRemoteSources(playerManager,
                com.sedmelluq.discord.lavaplayer.source.youtube.YoutubeAudioSourceManager.class);
        return playerManager;
    }

    @Bean
    public AudioPlayer getPlayerManager() {
        var playerManager = getAudioPlayerManager();
        return playerManager.createPlayer();
    }

    @Bean
    public LavalinkClient getLavalinkClient(ApplicationArguments args) {
        return this.lavalinkClient;
    }

}
