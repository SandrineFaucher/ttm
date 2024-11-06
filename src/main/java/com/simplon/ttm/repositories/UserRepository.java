package com.simplon.ttm.repositories;


import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.simplon.ttm.models.User;
import com.simplon.ttm.models.UserRole;

public interface UserRepository extends JpaRepository <User, Long>{

    User findByRole(UserRole role);

    Optional<User>findByUsername(String username);
    
}
