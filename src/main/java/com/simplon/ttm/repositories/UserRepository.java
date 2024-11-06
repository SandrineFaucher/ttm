package com.simplon.ttm.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.simplon.ttm.models.User;

public interface UserRepository extends JpaRepository <User, Long>{
    
}
