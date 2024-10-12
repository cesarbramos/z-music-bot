package com.goat.z_music.utils;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class GuildManagerProviderService {

    private final Map<Long, GuildMusicManager> musicManagers = new HashMap<>();

    public void put(Long guildId, GuildMusicManager mng) {
        this.musicManagers.put(guildId, mng);
    }

    public GuildMusicManager get(Long guildId) {
        return musicManagers.get(guildId);
    }

    public GuildMusicManager remove(Long guildId) {
        return this.musicManagers.remove(guildId);
    }



}
