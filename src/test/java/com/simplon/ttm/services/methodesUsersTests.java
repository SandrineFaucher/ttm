package com.simplon.ttm.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.simplon.ttm.dto.RegisterDto;
import com.simplon.ttm.models.User;
import com.simplon.ttm.models.UserRole;
import com.simplon.ttm.repositories.UserRepository;
import com.simplon.ttm.services.impl.UserServiceImpl;

public class methodesUsersTests {

    @ExtendWith(MockitoExtension.class)
       
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userServiceImpl;

    @Test
    void saveGodparent(){
        //given
        LocalDate date = LocalDate.parse("2024-11-06");
        User godparent = User.builder()
                .id(1L)
                .username("Parain")
                .password("parain123")
                .role(UserRole.GODPARENT)
                .creationDate(date)
                .build();
        RegisterDto godparentDto = RegisterDto.builder()
            .username("Parain")
            .password("parain123")
            .role(UserRole.GODPARENT)
            .build();

        //when
        when(userRepository.save(any(User.class))).thenReturn(godparent);
        
        //then
        User user = userServiceImpl.saveGodparent(godparentDto);
        assertEquals(godparent, user);
    }

    @Test
    void saveLeaderProject(){
        //given
        LocalDate date = LocalDate.parse("2024-11-06");
        User leaderProject = User.builder()
                .id(1L)
                .username("LeaderProect")
                .password("lp123")
                .role(UserRole.LEADERPROJECT)
                .creationDate(date)
                .build();
        RegisterDto leaderProjectDto = RegisterDto.builder()
                .username("Leaderproject")
                .password("lp123")
                .role(UserRole.LEADERPROJECT)
                .build();
        //when
        when(userRepository.save(any(User.class))).thenReturn(leaderProject);

        //then
        User user = userServiceImpl.saveLeaderProject(leaderProjectDto);

        assertEquals(leaderProject, user);
    }

    @Test
    void saveAdmin(){
        //given
        LocalDate date = LocalDate.parse("2024-11-06");
        User admin = User.builder()
                .id(1L)
                .username("Admin")
                .password("admin123")
                .role(UserRole.ADMIN)
                .creationDate(date)
                .build();
        RegisterDto admintDto = RegisterDto.builder()
                .username("Admin")
                .password("admin123")
                .role(UserRole.ADMIN)
                .build();
        //when
        when(userRepository.save(any(User.class))).thenReturn(admin);

        //then
        User user = userServiceImpl.saveAdmin(admintDto);

        assertEquals(admin, user);
    }

    @Test
    void saveUser(){
        //given
        LocalDate date = LocalDate.parse("2024-11-06");
        User user = User.builder()
                .id(1L)
                .username("User")
                .password("user123")
                .role(UserRole.USER)
                .creationDate(date)
                .build();
        RegisterDto userDto = RegisterDto.builder()
                .username("User")
                .password("user123")
                .role(UserRole.USER)
                .build();
        //when
        when(userRepository.save(any(User.class))).thenReturn(user);

        //then
        User userTest = userServiceImpl.saveUser(userDto);

        assertEquals(userTest, user);
    }

    @Test 
    void getUserByRole(){
        //given
        LocalDate date = LocalDate.parse("2024-11-06");
        User user = User.builder()
                .id(1L)
                .username("User")
                .password("user123")
                .role(UserRole.USER)
                .creationDate(date)
                .build();
        User admin = User.builder()
                .id(1L)
                .username("Admin")
                .password("admin123")
                .role(UserRole.ADMIN)
                .creationDate(date)
                .build();
        User leaderProject = User.builder()
                .id(1L)
                .username("LeaderProect")
                .password("lp123")
                .role(UserRole.LEADERPROJECT)
                .creationDate(date)
                .build();
        User godparent = User.builder()
                .id(1L)
                .username("Parain")
                .password("parain123")
                .role(UserRole.GODPARENT)
                .creationDate(date)
                .build();
        //when
        when(userRepository.findByRole(UserRole.USER)).thenReturn(Optional.of(user));
        when(userRepository.findByRole(UserRole.ADMIN)).thenReturn(Optional.of(admin));
        when(userRepository.findByRole(UserRole.LEADERPROJECT)).thenReturn(Optional.of(leaderProject));
        when(userRepository.findByRole(UserRole.GODPARENT)).thenReturn(Optional.of(godparent));

        //then
        User userTestRole = userServiceImpl.getUserByRole(UserRole.USER);
        assertEquals(userTestRole.getUsername(), "User");

        User adminTestRole = userServiceImpl.getUserByRole(UserRole.ADMIN);
        assertEquals(adminTestRole.getUsername(), "Admin");

        User leaderTestRole  = userServiceImpl.getUserByROle(UserRole.LEADERPROJECT);
        assertEquals(leaderTestRole.getUsername(), "LeaderProect");

        User godparentTestRole = userServiceImpl.getUserByRole(UserRole.GODPARENT);
        assertEquals(godparentTestRole.getUsername(), "Parain");
    };
}


