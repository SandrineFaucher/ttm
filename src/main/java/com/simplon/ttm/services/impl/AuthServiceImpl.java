package com.simplon.ttm.services.impl;

import java.util.Optional;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
    private final UserRepository userRepository;
    private AuthenticationManager authenticationManager;

    public String login(LoginDto loginDto){
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginDto.getUsername(),
                        loginDto.getPassword()
                )
        );
            return jwtUtils.generateToken(loginDto.getUsername());
    }
}
