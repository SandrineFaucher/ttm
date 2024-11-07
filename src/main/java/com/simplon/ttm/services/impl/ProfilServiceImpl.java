package com.simplon.ttm.services.impl;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.simplon.ttm.models.Profil;
import com.simplon.ttm.models.User;
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
}
