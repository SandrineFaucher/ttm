package com.simplon.ttm.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.simplon.ttm.dto.SectorDto;
import com.simplon.ttm.services.SectorService;

import jakarta.validation.Valid;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
public class SectorController {
    private final SectorService sectorService;

    public SectorController(SectorService sectorService){
    this.sectorService = sectorService;
}
    @PostMapping("/sector")
    public ResponseEntity<String> createSector(@Valid @RequestBody SectorDto sectorDto){
        sectorService.saveSector(sectorDto);
        return ResponseEntity.ok("Sector created successfully");
    }


}
