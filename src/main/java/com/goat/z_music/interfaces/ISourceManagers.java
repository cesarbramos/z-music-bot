package com.goat.z_music.interfaces;

import com.sedmelluq.discord.lavaplayer.source.AudioSourceManager;

import java.util.Optional;

public interface ISourceManagers {

    <T extends AudioSourceManager> Optional<T> getSourceManager(Class<T> sourceClass);

}
