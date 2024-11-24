package com.simplon.ttm.services.impl;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.simplon.ttm.models.Accompaniement;
import com.simplon.ttm.models.Profil;
import com.simplon.ttm.models.Sector;
import com.simplon.ttm.models.User;
import com.simplon.ttm.models.UserRole;
import com.simplon.ttm.repositories.ProfilRepository;
import com.simplon.ttm.repositories.UserRepository;
import com.simplon.ttm.services.ProfilService;

import jakarta.transaction.Transactional;

@Service
public class ProfilServiceImpl implements ProfilService {

    private UserRepository userRepository;
    private ProfilRepository profilRepository;

    @Autowired
    public ProfilServiceImpl(UserRepository userRepository, ProfilRepository profilRepository) {
        this.userRepository = userRepository;
        this.profilRepository = profilRepository;
    }

    @Transactional
    public Profil saveUserProfil(Profil profil) {
        // Récupération du user à associer au profil
        User user = userRepository.findById(profil.getUser().getId())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        // Associer le profil au user par son attribut user
        profil.setUser(user);

        // Sauvegarder le profil
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
