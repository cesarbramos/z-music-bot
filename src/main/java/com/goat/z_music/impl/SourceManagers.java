package com.goat.z_music.impl;

import com.goat.z_music.interfaces.ISourceManagers;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SourceManagers implements ISourceManagers {
    private final AudioPlayerManager manager;

    public <T extends AudioSourceManager> Optional<T> getSourceManager(Class<T> sourceClass) {
        return manager.getSourceManagers().stream()
                .filter(sourceClass::isInstance)
                .map(x -> (T) x)
                .findFirst();
    }

}
