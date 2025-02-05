package com.simplon.ttm.services;


import java.util.List;
import java.util.Optional;

import com.simplon.ttm.dto.SectorDto;
import com.simplon.ttm.models.Sector;

public interface SectorService {

    void saveSector(SectorDto sectorDto);

    Optional<Sector> getById(Long id);

    Sector updateSector(Sector sector, SectorDto sectorDto);

    void deleteSector(Long id);

    List<Sector> findAll();
}

