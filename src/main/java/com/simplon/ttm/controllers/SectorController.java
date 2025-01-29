package com.simplon.ttm.controllers;

import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.simplon.ttm.dto.SectorDto;
import com.simplon.ttm.models.Sector;
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

    @GetMapping("/sector/{id}")
    public ResponseEntity<Sector>getSectorById(@PathVariable Long id){
        Optional<Sector> sector  = sectorService.getById(id);
        if (sector.isPresent()) {
            return ResponseEntity.ok(sector.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
    @PutMapping("/sectorUpdate/{id}")
    public ResponseEntity<Sector> updateSectorById(@PathVariable Long id, @RequestBody SectorDto sectorDto){
        Optional<Sector> sectorOptional = sectorService.getById(id);
        if (sectorOptional.isPresent()) {
            Sector updatedSector = sectorService.updateSector(sectorOptional.get(), sectorDto);
            return ResponseEntity.ok(updatedSector);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }


}
