package com.goat.z_music;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class ZMusicApplication {

	public static void main(String[] args) {
		SpringApplication.run(ZMusicApplication.class, args);
	}

}
