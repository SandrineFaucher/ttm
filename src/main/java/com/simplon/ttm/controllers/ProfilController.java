package com.simplon.ttm.controllers;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import com.simplon.ttm.dto.ProfilDto;
import com.simplon.ttm.dto.UpdateProfilDto;
import com.simplon.ttm.models.Profil;
import com.simplon.ttm.models.User;
import com.simplon.ttm.repositories.ProfilRepository;
import com.simplon.ttm.services.FileService;
import com.simplon.ttm.services.ProfilService;
import com.simplon.ttm.services.UserService;



@CrossOrigin(origins = "http://localhost:5173" ,allowCredentials = "true")
@RestController
public class ProfilController {
    private  ProfilRepository profilRepository;

    private  ProfilService profilService;

    private  UserService userService;

    private FileService fileService;

    public ProfilController(ProfilRepository profilRepository, ProfilService profilService, UserService userService, FileService fileService){
        this.profilRepository = profilRepository;
        this.profilService = profilService;
        this.userService = userService;
        this.fileService = fileService;
    }

    @PostMapping(value = "/profil", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Map<String, Object>> createProfil(@ModelAttribute ProfilDto profilDto,
            @RequestPart(value = "image", required = false) MultipartFile image) throws IOException {

        // Récupération du user authentifié
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String authenticatedUsername = authentication.getName();

        // Recherche de l'utilisateur en base de données
        User user = userService.findByUsername(authenticatedUsername)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        System.out.println("Utilisateur authentifié : " + authenticatedUsername + " (ID: " + user.getId() + ")");

        // Associer l'ID de l'utilisateur au profilDto
        profilDto.setUserId(user.getId());

        // Gestion de l'image (optionnelle)
        String imageUrl = null;
        if (image != null && !image.isEmpty()) {
            imageUrl = fileService.saveFile(image, "profil_images");
            System.out.println("Image enregistrée : " + imageUrl);
        }

        // Sauvegarde du profil
        Profil savedProfil = profilService.saveUserProfil(profilDto, image);

        // Création de la réponse
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Profil créé avec succès");
        response.put("profil", savedProfil);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping(value = "/updateProfil", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Map<String, Object>> updateProfil(@ModelAttribute UpdateProfilDto updateProfilDto,
            @RequestPart(value = "image", required = false) MultipartFile image) throws IOException {

        // Récupération de l'utilisateur authentifié
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String authenticatedUsername = authentication.getName();

        // Recherche de l'utilisateur en base de données
        User user = userService.findByUsername(authenticatedUsername)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        // Vérifier que l'utilisateur possède un profil à mettre à jour
        Optional<Profil> existingProfil = profilService.getProfilByUserId(user.getId());


        // Mise à jour du profil
        Profil updatedProfil = profilService.updateUserProfil(existingProfil, updateProfilDto, image);

        // Création de la réponse
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Profil mis à jour avec succès");
        response.put("profil", updatedProfil);

        return ResponseEntity.ok(response);
    }
    @GetMapping("/usersProfils/by-role")
    public ResponseEntity<List<User>> getUsersProfilsByRole(Authentication authentication) {
        // Récupération de l'utilisateur connecté
        String currentUsername = authentication.getName();

        // Récupération de la liste des utilisateurs selon la logique de rôle
        List<User> users = userService.getUsersVisibleToCurrentUser(currentUsername);
        // Afficher le résultat en console
        System.out.println(users);
        return ResponseEntity.ok(users);
    }
}
