package com.simplon.ttm.controllers;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.simplon.ttm.dto.ProfilDto;
import com.simplon.ttm.models.Profil;
import com.simplon.ttm.models.User;
import com.simplon.ttm.repositories.ProfilRepository;
import com.simplon.ttm.services.ProfilService;
import com.simplon.ttm.services.UserService;

import jakarta.validation.Valid;

@CrossOrigin(origins = "http://localhost:5173" ,allowCredentials = "true")
@RestController
public class ProfilController {
    private  ProfilRepository profilRepository;

    private  ProfilService profilService;

    private  UserService userService;

    public ProfilController(ProfilRepository profilRepository, ProfilService profilService, UserService userService){
        this.profilRepository = profilRepository;
        this.profilService = profilService;
        this.userService = userService;

    }

    @PostMapping("/profil")
    public ResponseEntity<Map<String, Object>> createProfil(@Valid @RequestBody ProfilDto profilDto) {
        // Récupération du user authentifié
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String authenticatedUsername = authentication.getName();

        // Recherche de l'utilisateur en base de données
        User user = userService.findByUsername(authenticatedUsername)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        System.out.println("Utilisateur authentifié : " + authenticatedUsername + " (ID: " + user.getId() + ")");

        // Associer l'ID de l'utilisateur au profilDto
        profilDto.setUserId(user.getId());

        // Sauvegarde du profil
        Profil savedProfil = profilService.saveUserProfil(profilDto);

        // Création de la réponse
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Profil créé avec succès");
        response.put("profil", savedProfil);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

}
