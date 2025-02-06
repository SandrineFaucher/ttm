package com.simplon.ttm.services.impl;


import java.util.List;
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
        // Récupération du user connecté à associer au profil
        User user = userRepository.findById(profilDto.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        // Créer un nouvel objet Profil à partir du ProfilDto
        Profil profil = new Profil();
        profil.setAvailability(profilDto.getAvailability());
        profil.setContent(profilDto.getContent());
        profil.setCity(profilDto.getCity());
        profil.setDepartment(profilDto.getDepartment());
        profil.setRegion(profilDto.getRegion());
        profil.setImage(profilDto.getImage());

        // Convertir les IDs des secteurs en entités Sector
        List<Sector> sectors = profilDto.getSectorIds().stream()
                .map(sectorId -> sectorRepository.findById(Long.parseLong(sectorId))
                        .orElseThrow(() -> new IllegalArgumentException("Sector not found")))
                .collect(Collectors.toList());

        // Convertir les IDs des accompagnements en entités Accompaniement
        List<Accompaniement> accompaniements = profilDto.getAccompaniementIds().stream()
                .map(accompaniementId -> accompaniementRepository.findById(Long.parseLong(accompaniementId))
                        .orElseThrow(() -> new IllegalArgumentException("Accompaniement not found")))
                .collect(Collectors.toList());
        // Associer les secteurs et les accompagnements au profil
        profil.setSector(sectors);
        profil.setAccompaniement(accompaniements);

        // Associer le profil à l'utilisateur
        profil.setUser(user);

        // Sauvegarder le profil du user
        return profilRepository.save(profil);
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
        List<Profil> profils = profilRepository.findAllBySector(sector);
        return profils;
    }

    public List<Profil> getAllProfilByAccompaniement(Accompaniement accompaniement) {
        List<Profil> profils = profilRepository.findAllByAccompaniement(accompaniement);
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
