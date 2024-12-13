package com.simplon.ttm.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
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
import org.springframework.security.crypto.password.PasswordEncoder;

import com.simplon.ttm.dto.RegisterDto;
import com.simplon.ttm.models.User;
import com.simplon.ttm.models.UserRole;
import com.simplon.ttm.repositories.UserRepository;
import com.simplon.ttm.services.impl.UserServiceImpl;

public class UsersTests {

    @ExtendWith(MockitoExtension.class)
       
    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userServiceImpl;

    @Test
    /**
     * Teste la méthode qui crée un user avec son role
     */
    void saveUserWithRole(){
        // Given
        LocalDate date = LocalDate.parse("2024-11-06");
        User godparent = User.builder()
                .id(1L)
                .username("Parain")
                .password(passwordEncoder.encode("parain123"))
                .role(UserRole.GODPARENT)
                .creationDate(date)
                .build();
        RegisterDto godparentDto = RegisterDto.builder()
                .username("Parain")
                .password("parain123") // Gardez le mot de passe non encodé pour le DTO
                .passwordConfirm("parain123") // Confirmez que les mots de passe correspondent
                .role(UserRole.GODPARENT) // Transmettez le rôle comme chaîne
                .build();

        // When
        when(passwordEncoder.encode("parain123")).thenReturn(godparent.getPassword());
        when(userRepository.save(any(User.class))).thenReturn(godparent);

        // Then
        User user = userServiceImpl.saveUserWithRole(godparentDto);
        assertNotNull(user); // Vérifie que l'utilisateur n'est pas null
        assertEquals(godparent.getUsername(), user.getUsername()); // Vérifie le nom d'utilisateur
        assertEquals(godparent.getRole(), user.getRole()); // Vérifie le rôle
        assertEquals(godparent.getCreationDate(), user.getCreationDate()); // Vérifie la date de création
    }


    @Test
    /**
     * Teste la méthode qui récupère un user par son username
     */
    void getUserbyUsername()
    {
        //given
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
                .role(UserRole.ADMIN) 
                .build();      
        //when
        when(userRepository.findById(1L)).thenReturn(Optional.of(userById));

        //then
        Optional<User> user = userServiceImpl.getUserById(1L);
        assertEquals(UserRole.ADMIN, user.orElseThrow().getRole());
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


