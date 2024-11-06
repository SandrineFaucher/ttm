package com.simplon.ttm.repositories;


import org.springframework.data.jpa.repository.JpaRepository;

import com.simplon.ttm.models.User;
import com.simplon.ttm.models.UserRole;

public interface UserRepository extends JpaRepository <User, Long>{

    User findByRole(UserRole role);
    
}
