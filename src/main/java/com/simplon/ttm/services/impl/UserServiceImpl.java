package com.simplon.ttm.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.simplon.ttm.dto.RegisterDto;
import com.simplon.ttm.models.User;
import com.simplon.ttm.models.UserRole;
import com.simplon.ttm.repositories.UserRepository;
import com.simplon.ttm.services.UserService;

@Service
public class UserServiceImpl implements UserService {
    
    private UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    public User saveGodparent(RegisterDto user) {
        // TODO Auto-generated method stub
        
            User godeparent = User.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .role(UserRole.GODPARENT)
                .build();
            return userRepository.save(godeparent);
     
    }
}

