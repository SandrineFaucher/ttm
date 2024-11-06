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
           
            User godeparent = User.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .role(UserRole.GODPARENT)
                .build();
            return userRepository.save(godeparent);
     
    }

    public User saveLeaderProject(RegisterDto user){
            User leaderProject = User.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .role(UserRole.LEADERPROJECT)
                .build();
            return userRepository.save(leaderProject);
    }

    public User saveAdmin(RegisterDto user) {
        User admin = User.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .role(UserRole.ADMIN)
                .build();
            return userRepository.save(admin);
    }

    public User saveUser(RegisterDto userDto) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'saveUser'");
    }
}

