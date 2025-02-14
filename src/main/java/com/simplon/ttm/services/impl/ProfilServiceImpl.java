package com.simplon.ttm.services.impl;


import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.simplon.ttm.dto.ProfilDto;
import com.simplon.ttm.models.Accompaniement;
import com.simplon.ttm.models.Profil;
import com.simplon.ttm.models.Sector;
import com.simplon.ttm.models.User;
import com.simplon.ttm.models.UserRole;
import com.simplon.ttm.repositories.AccompaniementRepository;
import com.simplon.ttm.repositories.ProfilRepository;
import com.simplon.ttm.repositories.SectorRepository;
import com.simplon.ttm.repositories.UserRepository;
import com.simplon.ttm.services.ProfilService;

import jakarta.transaction.Transactional;

@Service
public class ProfilServiceImpl implements ProfilService {

    private  UserRepository userRepository;
    private   ProfilRepository profilRepository;

    private  SectorRepository sectorRepository;

    private  AccompaniementRepository accompaniementRepository;


    @Autowired
    public ProfilServiceImpl(UserRepository userRepository,
                             ProfilRepository profilRepository,
                             SectorRepository sectorRepository,
                             AccompaniementRepository accompaniementRepository) {
        this.userRepository = userRepository;
        this.profilRepository = profilRepository;
        this.sectorRepository = sectorRepository;
        this.accompaniementRepository = accompaniementRepository;
    }

    @Transactional
    public Profil saveUserProfil(ProfilDto profilDto) {
        // Récupère le user à associer au profil
        User user = userRepository.findById(profilDto.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        // Vérifier si un profil existe déjà pour cet utilisateur
        if (user.getProfil() != null) {
            throw new IllegalArgumentException("L'utilisateur a déjà un profil");
        }

        // Crée un nouvel objet Profil à partir du ProfilDto
        Profil profil = new Profil();
        profil.setAvailability(profilDto.getAvailability());
        profil.setContent(profilDto.getContent());
        profil.setCity(profilDto.getCity());
        profil.setDepartment(profilDto.getDepartment());
        profil.setRegion(profilDto.getRegion());
        profil.setImage(profilDto.getImage());

        // Récupère les secteurs sélectionnés par l'utilisateur
        List<Sector> sectors = Optional.ofNullable(profilDto.getSectors())
                .filter(ids -> !ids.isEmpty())  // Évite une requête inutile si la liste est vide
                .map(sectorRepository::findAllById) // Récupère tous les secteurs par leurs ID
                .orElseThrow(() -> new IllegalArgumentException("Un ou plusieurs secteurs sélectionnés n'existent pas"));

        // Vérifie que tous les secteurs demandés ont bien été trouvés
        if (sectors.size() != profilDto.getSectors().size()) {
            throw new IllegalArgumentException("Un ou plusieurs secteurs sélectionnés sont invalides");
        }

        // Associe les secteurs au profil
        profil.setSectors(sectors);

        // Récupère les accompagnements sélectionnés par l'utilisateur
        List<Accompaniement> accompaniements = Optional.ofNullable(profilDto.getAccompaniements())
                .filter(ids -> !ids.isEmpty())  // Évite une requête inutile si vide
                .map(accompaniementRepository::findAllById) // Récupère tous les accompagnements par leurs ID
                .orElseThrow(() -> new IllegalArgumentException("Un ou plusieurs accompagnements sélectionnés n'existent pas"));

        // Vérifie que tous les accompagnements demandés ont bien été trouvés
        if (accompaniements.size() != profilDto.getAccompaniements().size()) {
            throw new IllegalArgumentException("Un ou plusieurs accompagnements sélectionnés n'existent pas");
        }

        // Associe les accompagnements au profil
        profil.setAccompaniements(accompaniements);

        // Associer le profil à l'utilisateur
        profil.setUser(user);

        // Sauvegarde d'abord le profil
        profil = profilRepository.save(profil);

        // Met à jour l'utilisateur avec le profil
        user.setProfil(profil);

        // Sauvegarde l'utilisateur pour établir la relation bidirectionnelle
        userRepository.save(user);

        return profil;
    }

    public List<Profil> getAllProfilsByRole(UserRole userRole) {
        
        List<Profil> profils = profilRepository.findAllByUserRole(userRole);
        return  profils;
    }

	public Profil getProfilByUserId(long userId) {
		Profil profil = profilRepository.findByUserId(userId);
        return profil;
	}

    public List<Profil> getAllProfilBySector(Sector sector) {
        List<Profil> profils = profilRepository.findAllBySectors(sector);
        return profils;
    }

    public List<Profil> getAllProfilByAccompaniement(Accompaniement accompaniement) {
        List<Profil> profils = profilRepository.findAllByAccompaniements(accompaniement);
        return profils;
    }

    public List<Profil> getAllProfilsByCity(String city) {
        List<Profil> profils = profilRepository.findAllByCity(city);
        return profils;
    }

    public List<Profil> getAllProfilsByDepartment(String department) {
        List<Profil> profils = profilRepository.findAllByDepartment(department);
        return profils; 
    }

    public List<Profil> getAllProfilsByRegion(String region) {
        List<Profil> profils = profilRepository.findAllByRegion(region);
        return profils;
    }
}
