package com.simplon.ttm.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.HashSet;
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
    /**
     * Teste la méthode qui crée un parain
     */
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
    /**
     * Teste la méthode qui crée un porteur de projet 
     */
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
    /**
     * Teste la méthode qui crée un admin
     */
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
    /**
     * Teste la méthode qui crée un user (ex : employé initiative Deux-Sèvres)
     */
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
    /**
     * Teste la méthode qui récupère un user par son role
     */
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
        Optional<User> userTestRole = userServiceImpl.getUserByRole(UserRole.USER);
        assertEquals(userTestRole.orElseThrow().getUsername(), "User");

        Optional<User> adminTestRole = userServiceImpl.getUserByRole(UserRole.ADMIN);
        assertEquals(adminTestRole.orElseThrow().getUsername(), "Admin");

        Optional<User> leaderTestRole  = userServiceImpl.getUserByRole(UserRole.LEADERPROJECT);
        assertEquals(leaderTestRole.orElseThrow().getUsername(), "LeaderProect");

        Optional<User> godparentTestRole = userServiceImpl.getUserByRole(UserRole.GODPARENT);
        assertEquals(godparentTestRole.orElseThrow().getUsername(), "Parain");
    };

    @Test
    /**
     * Teste la méthode qui récupère un user par son username
     */
    void getUserbyUsername()
    {
        //given
        LocalDate date = LocalDate.parse("2024-11-05");
        User userByName = User.builder()
                .id(1L)
                .username("faucher")
                .build();
                
        //when
        when(userRepository.findByUsername("faucher")).thenReturn(Optional.of(userByName));

        //then
        Optional<User> user = userServiceImpl.getUserByUsername("faucher");
        assertEquals("faucher", user.orElseThrow().getUsername());
    }

    @Test
    /**
     * Teste la méthode qui récupère un user par son id
     */
    void getUserbyId()
    {
        //given
        User userById = User.builder()
                .id(1L)
                .username("faucher")
                .password("test123")
                .role(UserRole.ADMIN) 
                .build();      
        //when
        when(userRepository.findById(1L)).thenReturn(Optional.of(userById));

        //then
        Optional<User> user = userServiceImpl.getUserById(1L);
        assertEquals("test123", user.orElseThrow().getPassword());
    }

    @Test
    /**
     * Teste la méthode qui sauve deux users par leur id dans une table intermédiaire
     */
    void saveMatch(){
        //given
        // construction de deux users avec des id différents
        User user1 = User.builder().id(1L).user1(new HashSet<>()).user2(new HashSet<>()).build();
        User user2 = User.builder().id(3L).user1(new HashSet<>()).user2(new HashSet<>()).build();

        //when
        when(userRepository.findById(1L)).thenReturn(Optional.of(user1));
        when(userRepository.findById(3L)).thenReturn(Optional.of(user2));

        //then
        boolean result = userServiceImpl.saveMatch(1L, 3L);
        assertTrue(result);
        verify(userRepository).findById(1L);
        verify(userRepository).findById(3L);
        verify(userRepository).save(user1);
        verify(userRepository).save(user2);

    }
}


