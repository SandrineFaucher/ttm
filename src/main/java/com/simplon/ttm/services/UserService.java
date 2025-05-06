package com.simplon.ttm.services;

import java.util.List;
import java.util.Optional;

import org.springframework.security.access.prepost.PreAuthorize;

import com.simplon.ttm.dto.RegisterDto;
import com.simplon.ttm.dto.UpdatePasswordDto;
import com.simplon.ttm.dto.UserUpdateDto;
import com.simplon.ttm.models.User;



public interface UserService {

    User saveUserWithRole(RegisterDto user);

    List<User> getUsersVisibleToCurrentUser(String username);

    Optional<User> getUserByUsername(String username);

    Optional<User> getUserById(Long l);

    User updateUserByUsername(String username, UserUpdateDto userUpdateDto);

    Optional<User> findByUsername(String authenticatedUsername);

    User updatePasswordByUsername(String authenticatedUsername, UpdatePasswordDto updatePasswordDto);

    boolean saveMatch(long userId1, long userId2);
}

