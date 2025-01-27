package com.simplon.ttm.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.simplon.ttm.dto.SectorDto;
import com.simplon.ttm.models.Sector;
import com.simplon.ttm.repositories.SectorRepository;
import com.simplon.ttm.services.SectorService;

@Service
public class SectorServiceImpl implements SectorService {
    private SectorRepository sectorRepository;

    @Autowired
    public SectorServiceImpl(SectorRepository sectorRepository){
        this.sectorRepository = sectorRepository;
    }
    // Méthode pour sauvegarder un secteur
    public void saveSector(SectorDto sectorDto) {
        Sector sector = new Sector();
        sector.setContent(sectorDto.getContent());

        // Appel du repository pour enregistrer l'entité
        sectorRepository.save(sector);
    }

}
