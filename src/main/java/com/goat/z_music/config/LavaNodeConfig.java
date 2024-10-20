package com.goat.z_music.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
public class LavaNodeConfig {

    @Value("${lavalink.node.name}")
    private String nodeName;

    @Value("${lavalink.node.serverUri}")
    private String serverUri;

    @Value("${lavalink.node.password}")
    private String password;

}
