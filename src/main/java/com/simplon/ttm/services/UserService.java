package com.simplon.ttm.services;

import java.util.Optional;

import com.simplon.ttm.dto.RegisterDto;
import com.simplon.ttm.models.User;
import com.simplon.ttm.models.UserRole;


public interface UserService {
    User saveGodparent(RegisterDto userMapping);

    User saveLeaderProject(RegisterDto userMapping);

    User saveAdmin(RegisterDto userMapping);

    User saveUser(RegisterDto userMapping);

    Optional<User> getUserByRole(UserRole role);

    Optional<User> getUserByUsername(String username);

    Optional<User> getUserById(Long l);
}
