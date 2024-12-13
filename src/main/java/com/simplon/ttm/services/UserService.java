package com.simplon.ttm.services;

import java.util.List;
import java.util.Optional;

import org.springframework.security.core.Authentication;

import com.simplon.ttm.dto.RegisterDto;
import com.simplon.ttm.models.User;
import com.simplon.ttm.models.UserRole;


public interface UserService {
    User saveUserWithRole(RegisterDto user);

    List<User> getUsersVisibleToCurrentUser(String username);

    Optional<User> getUserByUsername(String username);

    Optional<User> getUserById(Long l);

    Optional<User> from(Authentication authentication);
}
