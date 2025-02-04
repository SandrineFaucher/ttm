package com.simplon.ttm.services.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.simplon.ttm.dto.SectorDto;
import com.simplon.ttm.models.Sector;
import com.simplon.ttm.repositories.SectorRepository;
import com.simplon.ttm.services.SectorService;

import jakarta.persistence.EntityNotFoundException;

@Service
public class SectorServiceImpl implements SectorService {
    private SectorRepository sectorRepository;

    @Autowired
    public SectorServiceImpl(SectorRepository sectorRepository){
        this.sectorRepository = sectorRepository;
    }

    /**
     * Méthode pour sauvegarder un secteur
     * @param sectorDto
     */
    public void saveSector(SectorDto sectorDto) {
        Sector sector = new Sector();
        sector.setContent(sectorDto.getContent());

        // Appel du repository pour enregistrer l'entité
        sectorRepository.save(sector);
    }

    /**
     * Méthode pour récupérer un secteur à partir de son id
     * @param id
     * @return
     */
    public Optional<Sector> getById(Long id){
        Optional<Sector> sector = sectorRepository.findById(id);
        if (sector.isEmpty()) {
            throw new EntityNotFoundException("Sector not found with id: " + id);
        }
        return sector;
    }

    /**
     * Méthode pour mettre à jour un secteur à partir du DTO
     * @param sector
     * @param sectorDto
     * @return
     */
    public Sector updateSector(Sector sector, SectorDto sectorDto) {
        sector.setContent(sectorDto.getContent());
        return sectorRepository.save(sector);
    }

    /**
     * Méthode pour récupérer la liste de tous les secteurs d'activité
     * @return
     */
    public List<Sector> findAll(){
        return sectorRepository.findAll();
    }

    /**
     * Méthode pour supprimer un secteur à partir de son id
     * @param id
     */
    public void deleteSector(Long id) {
        Sector sector = sectorRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Sector not found with id: " + id));
        sectorRepository.delete(sector);
    }
}
