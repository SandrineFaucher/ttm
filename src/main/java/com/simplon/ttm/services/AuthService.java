package com.simplon.ttm.services;

import java.util.Optional;

import org.springframework.security.core.Authentication;

import com.simplon.ttm.dto.LoginDto;
import com.simplon.ttm.models.User;

public interface AuthService {
    String login(LoginDto loginDto);

    Optional<User> from(Authentication authentication);
}
