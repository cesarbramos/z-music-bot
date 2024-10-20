package com.goat.z_music.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
public class RadioConfig {

    @Value("${radio.url}")
    private String radioUrl;

    @Value("${radio.extension}")
    private String radioExtension;

}
