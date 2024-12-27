

package com.simplon.ttm.controllers;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.simplon.ttm.dto.LoginDto;
import com.simplon.ttm.services.UserSecurityService;

import jakarta.validation.Valid;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private UserSecurityService userSecurityService;

    public AuthController(UserSecurityService userSecurityService, AuthenticationManager authenticationManager) {
        this.userSecurityService = userSecurityService;
        this.authenticationManager = authenticationManager;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid LoginDto loginDto) {
        try {
            // Authentifie l'utilisateur avec ses identifiants
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword())
            );

            // Si l'authentification réussit, le SecurityContext est mis à jour
            SecurityContextHolder.getContext().setAuthentication(authentication);

            // Récupère l'utilisateur authentifié
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            System.out.println(userDetails);

            // Retourne une réponse contenant des informations utiles (JWT, utilisateur, etc.)

            return ResponseEntity.ok().body(Map.of(
                    "message", "Login successful",
                    "username", userDetails.getUsername(),
                    "roles", userDetails.getAuthorities()
            ));
        } catch (Exception e) {
            // En cas d'échec d'authentification
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of(
                    "error", "Invalid username or password",
                    "details", e.getMessage()
            ));
        }
    }





}
