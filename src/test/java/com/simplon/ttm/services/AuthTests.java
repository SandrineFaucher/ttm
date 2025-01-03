package com.simplon.ttm.services;

import static org.hibernate.validator.internal.util.Contracts.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;

import com.simplon.ttm.models.User;
import com.simplon.ttm.models.UserRole;
import com.simplon.ttm.repositories.UserRepository;
import com.simplon.ttm.services.impl.AuthServiceImpl;


public class AuthTests {
    @ExtendWith(MockitoExtension.class)

    @Mock
    private UserRepository userRepository;

    @Mock
    private Authentication authentication;

    @Mock
    private UserDetails userDetails;

    @InjectMocks
    private AuthServiceImpl authServiceImpl;



    @Test
    public void testFrom_WithNullAuthentication() {
        Optional<User> result = authServiceImpl.from(null);
        assertFalse(result.isPresent(), "Expected empty Optional when authentication is null");
    }

    @Test
    public void testFrom_WithNoUserDetailsPrincipal() {
        // Simuler un user authentifié qui n'est pas une instance de UserDetails
        when(authentication.getPrincipal()).thenReturn(new Object());

        Optional<User> result = authServiceImpl.from(authentication);
        assertFalse(result.isPresent(), "Expected empty Optional when principal is not UserDetails");
    }

    @Test
    public void testFrom_WithValidAuthentication() {
        // Simuler un utilisateur avec un UserDetails valide
        String username = "testuser";
        User mockUser = User.builder()
                .id(2L)
                .username(username)
                .role(UserRole.GODPARENT)
                .build();

        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(userDetails.getUsername()).thenReturn(username);
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(mockUser));

        Optional<User> result = authServiceImpl.from(authentication);

        assertTrue(result.isPresent(), "Expected a non-empty Optional");
        assertEquals(mockUser, result.get(), "The result should match the mock user");
    }

    @Test
    public void testFrom_WithUserNotFound() {
        // Simuler un cas où l'utilisateur n'est pas trouvé
        String username = "nonexistentuser";

        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(userDetails.getUsername()).thenReturn(username);
        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());

        Optional<User> result = authServiceImpl.from(authentication);

        assertFalse(result.isPresent(), "Expected empty Optional when user is not found in the repository");
    }
}

