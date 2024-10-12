package com.goat.z_music.client;

import com.goat.z_music.dto.GenericData;
import com.goat.z_music.dto.SongDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Service
@FeignClient(name = "deezer", url = "${deezer.url}")
public interface DeezerClient {

    @GetMapping("/search?q={query}&limit=${deezer.search.limit}")
    GenericData<SongDTO> search(@RequestParam String query);

    @GetMapping("/search?q=artist:{artist} track:{query}&limit=${deezer.search.limit}")
    GenericData<SongDTO> search(@RequestParam String query, @RequestParam String artist);

    @GetMapping("/track/{id}")
    SongDTO findById(@RequestParam Long id);

}
