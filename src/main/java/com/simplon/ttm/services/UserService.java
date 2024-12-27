package com.simplon.ttm.services;

import java.util.List;
import java.util.Optional;

import com.simplon.ttm.dto.RegisterDto;
import com.simplon.ttm.models.User;



public interface UserService {
    User saveUserWithRole(RegisterDto user);

    List<User> getUsersVisibleToCurrentUser(String username);

    Optional<User> getUserByUsername(String username);

    Optional<User> getUserById(Long l);


}
