package com.goat.z_music.rest;

import com.github.topi314.lavasrc.deezer.DeezerAudioSourceManager;
import com.goat.z_music.dto.DeezerConfigDTO;
import com.goat.z_music.interfaces.ISourceManagers;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@org.springframework.web.bind.annotation.RestController
@RequestMapping("/config")
@RequiredArgsConstructor
@CrossOrigin(origins = "*", maxAge = 3600)
public class RestController {

    private final ISourceManagers sourceManagers;

    @PostMapping("/deezer")
    public ResponseEntity<DeezerConfigDTO> updateDeezerConfig(@RequestBody DeezerConfigDTO body) {
        var deezerManager = sourceManagers.getSourceManager(DeezerAudioSourceManager.class);
        deezerManager.ifPresent(deezer -> deezer.setArl(body.getArl()));

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/deezer")
    public ResponseEntity<DeezerConfigDTO> getDeezerConfig() {
        var deezerManager = sourceManagers.getSourceManager(DeezerAudioSourceManager.class);
        DeezerConfigDTO dto = new DeezerConfigDTO();
        deezerManager.ifPresent(conf -> dto.setArl(conf.getArl()));

        return ResponseEntity.ok(dto);
    }

}
