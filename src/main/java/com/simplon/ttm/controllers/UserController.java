package com.simplon.ttm.controllers;

import java.security.Principal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.simplon.ttm.dto.UpdatePasswordDto;
import com.simplon.ttm.dto.UserUpdateDto;
import com.simplon.ttm.models.User;
import com.simplon.ttm.repositories.UserRepository;
import com.simplon.ttm.services.UserService;

import jakarta.validation.Valid;
import lombok.Builder;

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

    @PostMapping("/match/{userId2}")
    public ResponseEntity<String> saveMatch(@PathVariable long userId2, Authentication authentication) {
        // Récupérer l'utilisateur authentifié
        String authenticatedUsername = authentication.getName();

        // Trouver l'utilisateur dans la base de données
        Optional<User> authenticatedUser = userRepository.findByUsername(authenticatedUsername);

        if (authenticatedUser.isPresent()) {
            long userId1 = authenticatedUser.get().getId();

            // Sauvegarder le match
            boolean isMatched = userService.saveMatch(userId1, userId2);

            if (isMatched) {
                return ResponseEntity.ok("Match enregistré avec succès !");
            } else {
                return ResponseEntity.badRequest().body("Impossible d'enregistrer le match.");
            }
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Utilisateur non authentifié.");
    }


    @GetMapping("/matches")
    public ResponseEntity<List<Long>> getUserMatches(Authentication authentication) {
        // Récupérer le nom d'utilisateur à partir du principal
        String authenticatedUsername = authentication.getName();

        // Trouver l'utilisateur dans la base de données
        Optional<User> authenticatedUser = userRepository.findByUsername(authenticatedUsername);

        if (authenticatedUser.isPresent()) {
            User user = authenticatedUser.get();

            List<Long> matchedUserIds = user.getUser1()
                    .stream()
                    .map(User::getId)
                    .collect(Collectors.toList());

            return ResponseEntity.ok(matchedUserIds);
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
}

