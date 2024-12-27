package com.simplon.ttm.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.simplon.ttm.dto.RegisterDto;
import com.simplon.ttm.services.UserService;

import jakarta.validation.Valid;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
public class AdminController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    public AdminController(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Méthode qui permet à l'admin de l'association de créer un user en fonction de son role
     * @param userMapping
     * @return user with role
     */
    @PostMapping("/admin/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody RegisterDto userMapping) {
        System.out.println("Ok");

        if (!userMapping.getPassword().equals(userMapping.getPasswordConfirm())) {
            return ResponseEntity.badRequest().body("Passwords do not match");
        }
        try {
            // Appel du service pour enregistrer l'utilisateur
            userService.saveUserWithRole(userMapping);
            return ResponseEntity.ok("User registered successfully!");
        } catch (Exception e) {
            // Gestion des erreurs
            System.err.println("Error during registration: " + e.getMessage());
            return ResponseEntity.status(500).body("Error during registration: " + e.getMessage());
        }
    }
}


