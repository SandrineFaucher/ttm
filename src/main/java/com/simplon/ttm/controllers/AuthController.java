package com.simplon.ttm.controllers;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jmx.export.annotation.ManagedOperation;
import org.springframework.security.core.Authentication;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.simplon.ttm.dto.LoginDto;

import com.simplon.ttm.models.User;
import com.simplon.ttm.repositories.UserRepository;
import com.simplon.ttm.services.AuthService;


import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@CrossOrigin(origins = "http://localhost:5173" ,allowCredentials = "true")
@RestController
public class AuthController {

    private AuthService authService;
    private UserRepository userRepository;

    //@PostMapping("/login")
    //    public ResponseEntity<?> login(@RequestBody LoginDto loginDto, HttpServletResponse response) {
    //        // Étape 1 : Authentifier l'utilisateur et générer un token
    //        String token = authService.login(loginDto);
    //
    //        // Étape 2 : Créer un cookie avec le token JWT
    //        Cookie cookie = new Cookie("JWT", token);  // Le nom du cookie est "JWT"
    //        cookie.setHttpOnly(true); // Empêche l'accès au cookie via JavaScript (sécurisé contre XSS)
    //        cookie.setSecure(true); // Assure que le cookie est uniquement envoyé via HTTPS (sécurisé contre les attaques man-in-the-middle)
    //        cookie.setPath("/"); // Le cookie sera envoyé pour toutes les routes
    //        cookie.setMaxAge(86400); // Durée du cookie en secondes (1 jour ici, ajuste selon tes besoins)
    //
    //        // Étape 3 : Ajouter le cookie dans la réponse
    //        response.addCookie(cookie);
    //
    //        // Étape 4 : Retourner une réponse avec un message de succès
    //        return ResponseEntity.ok("Login successful");  // Pas besoin de renvoyer le token, il est dans le cookie
    //    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody LoginDto loginDto, HttpServletResponse response) {
        //authentifie un utilisateur et génère un token
        String token = authService.login(loginDto);

        //crée un cookie qui transporte le token
        Cookie cookie = new Cookie( "JWT", token);
        cookie.setHttpOnly(true); // Empêche l'accès au cookie via JavaScript (sécurisé contre XSS)
        cookie.setSecure(true); // mettre la sécurité à "true" pour la production
        cookie.setPath("/"); // Le cookie sera envoyé pour toutes les routes
        cookie.setMaxAge(86400); // Durée du cookie en secondes (1 jour ici, ajuste selon tes besoins)

        // Ajoute le cookie à la response
        response.addCookie(cookie);

        // Retourne une réponse avec un message de succès et le nom de l'utilisateur
        Map<String, String> responseBody = new HashMap<>();
        responseBody.put("message", "Login successful");
        responseBody.put("username" , loginDto.getUsername());

        return ResponseEntity.ok(responseBody);
    }
    @ManagedOperation
    @GetMapping("/authenticate")
    public ResponseEntity<?> getAuthenticatedUser(Authentication authentication) {
        if (authentication == null || !(authentication.getPrincipal() instanceof UserDetails)) {
            // Si l'utilisateur n'est pas authentifié, renvoie un message d'erreur sous forme de String
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not authenticated");
        }

        // Récupère l'utilisateur authentifié
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        // Si vous souhaitez retourner une entité `User`, utilisez le repository pour la charger
        Optional<User> optionalUser = userRepository.findByUsername(userDetails.getUsername());
        if (optionalUser.isPresent()) {
            return ResponseEntity.ok(optionalUser.get());
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not found in database");
        }
    }
}






