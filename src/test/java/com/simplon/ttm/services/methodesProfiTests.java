package com.simplon.ttm.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.simplon.ttm.models.Profil;
import com.simplon.ttm.models.Sector;
import com.simplon.ttm.models.User;

import com.simplon.ttm.models.UserRole;
import com.simplon.ttm.repositories.ProfilRepository;
import com.simplon.ttm.repositories.UserRepository;
import com.simplon.ttm.services.impl.ProfilServiceImpl;

public class methodesProfiTests {

    @ExtendWith(MockitoExtension.class)

    @Mock
    private ProfilRepository profilRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private ProfilServiceImpl profilServiceImpl;

    @Test
    void saveUserProfil() {
        // given
        LocalDate date = LocalDate.parse("2024-11-07");
        User user = User.builder()
                .id(1L)
                .username("Parain")
                .password("parain123")
                .role(UserRole.GODPARENT)
                .creationDate(date)
                .build();
        Sector sector1 = Sector.builder()
                .id(2L)
                .content("Informaticien")
                .build();
        Sector sector2 = Sector.builder()
                .id(3L)
                .content("juridique")
                .build();

        // user = userRepository.findById(user.getId()).orElseThrow();

        Profil profil = Profil.builder()
                .id(2L)
                .availability("tous les jeudi")
                .sector(List.of(sector1, sector2))
                .city("Niort")
                .department("Deux-Sèvres")
                .region("Nouvelle Aquitaine")
                .creationDate(date)
                .user(user)
                .image("profil.png")
                .build();

        // when
        when(profilRepository.save(any(Profil.class))).thenReturn(profil);
        when(userRepository.findById(profil.getUser().getId())).thenReturn(Optional.of(user));
        // then
        Profil profilSaved = profilServiceImpl.saveUserProfil(profil);

        assertEquals(profilSaved.getAvailability(), "tous les jeudi");
        assertEquals(profilSaved.getUser().getUsername(), "Parain");
        
    }

}
