package com.simplon.ttm.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.simplon.ttm.dto.UpdatePasswordDto;
import com.simplon.ttm.dto.UserUpdateDto;
import com.simplon.ttm.models.User;
import com.simplon.ttm.repositories.UserRepository;
import com.simplon.ttm.services.UserService;

import jakarta.validation.Valid;
@CrossOrigin(origins = "http://localhost:5173" ,allowCredentials = "true")
@RestController

public class UserController {
    private UserService userService;
    private UserRepository userRepository;

    @Autowired
    public UserController(UserService userService, UserRepository userRepository) {
        this.userService = userService;
        this.userRepository = userRepository;
    }

    @GetMapping("/users")
    public Iterable<User> getAllUser() {
        return this.userRepository.findAll();
    }

    @GetMapping("/users/by-role")
    public ResponseEntity<List<User>> getUsersByRole(Authentication authentication) {
        // Récupération de l'utilisateur connecté
        String currentUsername = authentication.getName();

        // Récupération de la liste des utilisateurs selon la logique de rôle
        List<User> users = userService.getUsersVisibleToCurrentUser(currentUsername);
        return ResponseEntity.ok(users);
    }

    /**
     * @param userUpdateDTO
     * @return l'utilisateur avec les modifications eu usernme et de l'email
     */
    @PutMapping("/userUpdate")
    public ResponseEntity<User> updateUser(
            @Valid @RequestBody UserUpdateDto userUpdateDTO
    ) {
        // Récupération de l'utilisateur authentifié
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String authenticatedUsername = authentication.getName();
        System.out.println("Utilisateur authentifié : " + authenticatedUsername);
        // Appeler le service pour mettre à jour
        User updatedUser = userService.updateUserByUsername(authenticatedUsername, userUpdateDTO);

        return ResponseEntity.ok(updatedUser);
    }

    @PutMapping("/userPasswordUpdate")
    public ResponseEntity<User> updateUserPassword(
            @Valid @RequestBody UpdatePasswordDto updatePasswordDto
    ) {
        // Récupération de l'utilisateur authentifié
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String authenticatedUsername = authentication.getName();
        System.out.println("Utilisateur authentifié : " + authenticatedUsername);
        // Appeler le service pour mettre à jour le mot de passe
        User updateUserPassword = userService.updatePasswordByUsername(authenticatedUsername, updatePasswordDto);
        return ResponseEntity.ok(updateUserPassword);
    }
}

