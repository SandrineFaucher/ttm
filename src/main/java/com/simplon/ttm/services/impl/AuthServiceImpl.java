package com.simplon.ttm.services.impl;

import java.util.Optional;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.simplon.ttm.config.JwtUtils;
import com.simplon.ttm.dto.LoginDto;
import com.simplon.ttm.models.User;
import com.simplon.ttm.repositories.UserRepository;
import com.simplon.ttm.services.AuthService;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final JwtUtils jwtUtils;
    private AuthenticationManager authenticationManager;
    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;

    public String login(LoginDto loginDto){
        String password = passwordEncoder.encode(loginDto.getPassword());
        // Effectue l'authentification
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginDto.getUsername(),
                        loginDto.getPassword()
                )
        );

        // Place l'objet Authentication dans le SecurityContextHolder
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Génère un token JWT pour l'utilisateur authentifié
        return jwtUtils.generateToken(loginDto.getUsername());
    }

    public Optional<User> from(Authentication authentication) {
        if(authentication == null){
            return Optional.empty();
        }

        Object principal = authentication.getPrincipal();
        if(!(principal instanceof UserDetails)){
            return Optional.empty();
        }
        System.out.println(principal);

        UserDetails userDetails = (UserDetails)principal;
        return userRepository.findByUsername(userDetails.getUsername());
    }
}
