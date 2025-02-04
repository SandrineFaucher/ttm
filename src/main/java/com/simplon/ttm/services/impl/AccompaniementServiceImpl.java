package com.simplon.ttm.services.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.simplon.ttm.dto.AccompaniementDto;
import com.simplon.ttm.models.Accompaniement;
import com.simplon.ttm.repositories.AccompaniementRepository;
import com.simplon.ttm.services.AccompaniementService;

import jakarta.persistence.EntityNotFoundException;

@Service
public class AccompaniementServiceImpl implements AccompaniementService {
    private AccompaniementRepository accompaniementRepository;

    @Autowired
    public AccompaniementServiceImpl(AccompaniementRepository accompaniementRepository){
        this.accompaniementRepository = accompaniementRepository;
    }

    /**
     * Méthode pour sauvegarder un accompagnement
     * @param accompaniementDto
     */
    public void saveAccompaniement(AccompaniementDto accompaniementDto){
        Accompaniement accompaniement = new Accompaniement();
        accompaniement.setContent(accompaniementDto.getContent());

        accompaniementRepository.save(accompaniement);
    }

    /**
     * Méthode pour récupérer un accompagnement par son id
     * @param id
     * @return
     */
    public Optional<Accompaniement> getById(Long id){
        Optional<Accompaniement> accompaniement = accompaniementRepository.findById(id);
        if(accompaniement.isEmpty()){
            throw new EntityNotFoundException("Accompaniement not found wit id: " + id);
        }
        return accompaniement;
    }

    /**
     * Méthode pour mettre à jour un accompagnement
     * @param accompaniement
     * @param accompaniementDto
     * @return
     */
    public Accompaniement updateAccompaniement(Accompaniement accompaniement, AccompaniementDto accompaniementDto){
        accompaniement.setContent(accompaniementDto.getContent());
        return accompaniementRepository.save(accompaniement);
    }

    /**
     * Méthode pour retourner la liste des accompagnements
     * @return
     */
    public List<Accompaniement> findAll(){
        return accompaniementRepository.findAll();
    }

    /**
     * Méthode pour supprimer un accompagnement à partir de son id
     * @param id
     */
    public void deleteAccompaniement(Long id){
        Accompaniement accompaniement = accompaniementRepository.findById(id)
                .orElseThrow(()-> new EntityNotFoundException("Accompaniement not found with id: " + id));
        accompaniementRepository.delete(accompaniement);
    }
}
