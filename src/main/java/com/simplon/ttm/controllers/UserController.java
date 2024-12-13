package com.simplon.ttm.controllers;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


import com.simplon.ttm.models.User;
import com.simplon.ttm.repositories.UserRepository;
import com.simplon.ttm.services.UserService;

@RestController

public class UserController {
    private UserService userService;
    private UserRepository userRepository;
    public UserController(UserService userService, UserRepository userRepository){
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

}
