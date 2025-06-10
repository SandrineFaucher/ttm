package com.simplon.ttm.controllers;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.simplon.ttm.dto.RegisterDto;
import com.simplon.ttm.services.UserService;

import jakarta.validation.Valid;


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
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PostMapping("/admin/register")
    public ResponseEntity<Map<String, Object>> registerUser(@Valid @RequestBody RegisterDto userMapping) {
        System.out.println("Ok");

        Map<String , Object> response = new HashMap<>();

        if (!userMapping.getPassword().equals(userMapping.getPasswordConfirm())) {
            response.put("succes", false);
            response.put("message", "Passwords do not match");
            return ResponseEntity.badRequest().body(response);
        }
        try {
            userService.saveUserWithRole(userMapping);
            response.put("success", true);
            response.put("message", "User registered successfully!");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            System.err.println("Error during registration: " + e.getMessage());
            response.put("succes", false);
            response.put("message", "Error during registration: " + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }
}


