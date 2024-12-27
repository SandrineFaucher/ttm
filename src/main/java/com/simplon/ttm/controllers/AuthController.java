

package com.simplon.ttm.controllers;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.simplon.ttm.config.JwtAuthResponse;
import com.simplon.ttm.dto.LoginDto;
import com.simplon.ttm.services.AuthService;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
@RestController
public class AuthController {

    private AuthService authService;

    //    @PostMapping("/login")
    //    public ResponseEntity<JwtAuthResponse> login(@RequestBody LoginDto loginDto){
    //        String token = authService.login(loginDto);
    //
    //        JwtAuthResponse jwtAuthResponse = new JwtAuthResponse();
    //        jwtAuthResponse.setAccesToken(token);
    //        return new ResponseEntity<>(jwtAuthResponse, HttpStatus.OK);
    //    }
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
        // Étape 1 : Authentifier l'utilisateur et générer un token
        String token = authService.login(loginDto);

        // Étape 2 : Créer un cookie avec le token JWT
        Cookie cookie = new Cookie("JWT", token);  // Le nom du cookie est "JWT"
        cookie.setHttpOnly(true); // Empêche l'accès au cookie via JavaScript (sécurisé contre XSS)
        cookie.setSecure(true); // Assure que le cookie est uniquement envoyé via HTTPS (sécurisé contre les attaques man-in-the-middle)
        cookie.setPath("/"); // Le cookie sera envoyé pour toutes les routes
        cookie.setMaxAge(86400); // Durée du cookie en secondes (1 jour ici, ajuste selon tes besoins)

        // Étape 3 : Ajouter le cookie dans la réponse
        response.addCookie(cookie);

        // Étape 4 : Retourner une réponse avec un message de succès et le token dans un objet JSON
        Map<String, String> responseBody = new HashMap<>();
        responseBody.put("message", "Login successful");
        responseBody.put("token", token);  // Ajoute le token dans la réponse (optionnel si tu veux seulement le cookie)

        return ResponseEntity.ok(responseBody);  // Renvoyer le message et le token dans une réponse JSON
    }
}






