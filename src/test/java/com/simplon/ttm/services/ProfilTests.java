package com.simplon.ttm.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.simplon.ttm.dto.ProfilDto;
import com.simplon.ttm.models.Accompaniement;
import com.simplon.ttm.models.Profil;
import com.simplon.ttm.models.Sector;
import com.simplon.ttm.models.User;

import com.simplon.ttm.models.UserRole;
import com.simplon.ttm.repositories.AccompaniementRepository;
import com.simplon.ttm.repositories.ProfilRepository;
import com.simplon.ttm.repositories.SectorRepository;
import com.simplon.ttm.repositories.UserRepository;
import com.simplon.ttm.services.impl.ProfilServiceImpl;

public class ProfilTests {

        @ExtendWith(MockitoExtension.class)

        @Mock
        private ProfilRepository profilRepository;

        @Mock
        private UserRepository userRepository;

        @Mock
        private SectorRepository sectorRepository;

        @Mock
        private AccompaniementRepository accompaniementRepository;

        @InjectMocks
        private ProfilServiceImpl profilServiceImpl;

        @Test
        /**
         * Teste la sauvegarde du profil d'un user inscrit sur le site
         */
        void saveUserProfil() {
                // given
                LocalDateTime date = LocalDateTime.parse("2024-11-06T00:00:00");
                User user = User.builder()
                        .id(1L)
                        .username("Parain")
                        .password("parain123")
                        .role(UserRole.GODPARENT)
                        .build();
                Sector sector1 = Sector.builder()
                        .id(2L)
                        .content("Informaticien")
                        .build();
                Sector sector2 = Sector.builder()
                        .id(3L)
                        .content("juridique")
                        .build();
                Accompaniement accompaniement = Accompaniement.builder()
                        .id(6L)
                        .content("Accompagnement")
                        .build();

                // Créer un ProfilDto avec les données nécessaires
                ProfilDto profilDto = ProfilDto.builder()
                        .userId(user.getId()) // Associer l'ID de l'utilisateur
                        .availability("tous les jeudi")
                        .sectorIds(List.of("2", "3")) // Les ID des secteurs en String
                        .accompaniementIds(List.of("6"))
                        .content("Profil content")
                        .city("Niort")
                        .department("Deux-Sèvres")
                        .region("Nouvelle Aquitaine")
                        .image("profil.png")
                        .build();

                Profil profil = Profil.builder()
                        .id(2L)
                        .availability("tous les jeudi")
                        .sector(List.of(sector1, sector2))
                        .city("Niort")
                        .department("Deux-Sèvres")
                        .region("Nouvelle Aquitaine")
                        .createdAt(date)
                        .user(user)
                        .image("profil.png")
                        .build();

                // when
                when(profilRepository.save(any(Profil.class))).thenReturn(profil);
                when(userRepository.findById(profilDto.getUserId())).thenReturn(Optional.of(user));
                when(sectorRepository.findById(2L)).thenReturn(Optional.of(sector1));
                when(sectorRepository.findById(3L)).thenReturn(Optional.of(sector2));
                when(accompaniementRepository.findById(6L)).thenReturn(Optional.of(accompaniement));

                // then
                Profil profilSaved = profilServiceImpl.saveUserProfil(profilDto);

                assertEquals(profilSaved.getAvailability(), "tous les jeudi");
                assertEquals(profilSaved.getUser().getUsername(), "Parain");
                assertEquals(profilSaved.getSector().size(), 2); // Vérifie que les secteurs sont bien associés
                assertTrue(profilSaved.getSector().contains(sector1));
                assertTrue(profilSaved.getSector().contains(sector2));
        }

        @Test
        /**
         * Teste la récupération de tous les profils à partir d'un role
         */
        void getAllProfilsByRole() {
                // given
                // construction de users avec leur attribut role
                User user1 = User.builder().username("test1").role(UserRole.LEADERPROJECT).build();
                User user2 = User.builder().username("test2").role(UserRole.LEADERPROJECT).build();
                User user3 = User.builder().username("test3").role(UserRole.ADMIN).build();
                // construction des profils à partir des users ci-dessus
                Profil profil1 = Profil.builder().user(user1).build();
                Profil profil2 = Profil.builder().user(user2).build();
                Profil profil3 = Profil.builder().user(user3).build();
                // création d'une liste de profils
                List<Profil> profils = new ArrayList<>();
                profils.add(profil1);
                profils.add(profil2);

                // when
                when(profilRepository.findAllByUserRole(UserRole.LEADERPROJECT)).thenReturn(profils);

                // then
                List<Profil> results = profilServiceImpl.getAllProfilsByRole(UserRole.LEADERPROJECT);

                assertEquals(2, results.size());
                assertTrue(results.contains(profil1));
                assertTrue(results.contains(profil2));
                assertNotEquals(profils, results.contains(profil3));
        }

        @Test
        /**
         * Teste la récupération d'un profil par l'id de son user associé
         */
        void getProfilByUserId() {
                // given
                // construction du user avec son id
                User user = User.builder().id(5L).username("sandrine").build();
                // construction du profil de ce user
                Profil profil = Profil.builder().user(user).build();

                // when
                when(profilRepository.findByUserId(user.getId())).thenReturn(profil);

                // then
                Profil profilTest = profilServiceImpl.getProfilByUserId(5L);

                assertEquals(profilTest.getUser().getUsername(), "sandrine");
                assertEquals(profilTest.getUser().getId(), 5L);

        }

        @Test
        /**
         * Test la récupération des profils d'un meme secteur d'activité
        */
        void getAllProfilsBySector(){
                //given
                //construction de deux secteurs
                Sector secteur1 = Sector.builder().content("informatique").build();
                Sector secteur2 = Sector.builder().content("finance").build();
                Sector secteur3 = Sector.builder().content("economie").build();
        
                //construction d'une liste de secteurs 
                List<Sector> firstProfilSectors = new ArrayList<>();
                firstProfilSectors.add(secteur1);
                firstProfilSectors.add(secteur2);
                List<Sector> secondProfilSectors = new ArrayList<>();
                secondProfilSectors.add(secteur3);
                secondProfilSectors.add(secteur1);
                //construction de profils avec un même secteur d'activité
                Profil profil1 = Profil.builder().sector(firstProfilSectors).build();
                Profil profil2 = Profil.builder().sector(secondProfilSectors).build();

                //when
                //mock des profils avec le meme secteur "informatique"
                when(profilRepository.findAllBySector(secteur1)).thenReturn(List.of(profil1, profil2));

                //then
                List<Profil> result = profilServiceImpl.getAllProfilBySector(secteur1);

                assertNotNull(result);
                assertEquals(2, result.size());
                assertTrue(result.contains(profil1));
                assertTrue(result.contains(profil2));
        }

        @Test
        /**
        * Test la récupération des profils qui ont besoin du même accompagnement
        */
        void getAllProfilByAccompaniement(){
                //given
                Accompaniement accompaniement1 = Accompaniement.builder().content("informatique").build();
                Accompaniement accompaniement2 = Accompaniement.builder().content("economique").build();
                Accompaniement accompaniement3 = Accompaniement.builder().content("reseau").build();

                //construction d'une liste d'accommpaniement
                List<Accompaniement> firstProfilAccompaniements = new ArrayList<>();
                firstProfilAccompaniements.add(accompaniement1);
                firstProfilAccompaniements.add(accompaniement2);
                List<Accompaniement> secondProfilAccompaniements = new ArrayList<>();
                secondProfilAccompaniements.add(accompaniement1);
                secondProfilAccompaniements.add(accompaniement3);
                //construction de profils avec un même secteur d'activité
                Profil profil1 = Profil.builder().accompaniement(firstProfilAccompaniements).build();
                Profil profil2 = Profil.builder().accompaniement(secondProfilAccompaniements).build();

                //when
                when(profilRepository.findAllByAccompaniement(accompaniement1)).thenReturn(List.of(profil1, profil2));

                //then
                List<Profil> result = profilServiceImpl.getAllProfilByAccompaniement(accompaniement1);

                assertNotNull(result);
                assertEquals(2, result.size());
                assertTrue(result.contains(profil1));
                assertTrue(result.contains(profil2));
        }

        @Test
        /**
         * Teste la récupération des profils de la même ville
         */
        void getAllProfilsByCity(){
                //given
                //city
                String city = "Niort";
                //construction de profils avec la meme ville
                Profil profil1 = Profil.builder().city("Niort").build();
                Profil profil2 = Profil.builder().city("Niort").build();

                //when 
                when(profilRepository.findAllByCity(city)).thenReturn(List.of(profil1, profil2));

                //then
                List<Profil> result = profilServiceImpl.getAllProfilsByCity(city);

                assertNotNull(result);
                assertEquals(profil1.getCity(), "Niort");
                assertEquals(profil2.getCity(), "Niort");
                assertNotEquals(profil1.getCity(), "Angers");
        }

        @Test
        /**
         * Teste la récupération des profils du meme département
         */
        void getAllProfilsByDepartment(){
                //given
                //department
                String department = "Deux-Sèvres";
                //construction de profils avec le même département
                Profil profil1 = Profil.builder().department("Deux-Sèvres").build();
                Profil profil2 = Profil.builder().department("Deux-Sèvres").build();

                //when 
                when(profilRepository.findAllByDepartment(department)).thenReturn(List.of(profil1, profil2));

                //then
                List<Profil> result = profilServiceImpl.getAllProfilsByDepartment(department);

                assertNotNull(result);
                assertEquals(profil1.getDepartment(), "Deux-Sèvres");
                assertEquals(profil2.getDepartment(), "Deux-Sèvres");
                assertNotEquals(profil1.getDepartment(), "Angers");
        }

        @Test
        /**
         * Teste la récupération des profils d'une même région
         */
        void getAllProfilsByRegion(){
                //given
                //Region
                String region = "Nouvelle Aquitaine";
                //construction de profils avec la meme Région
                Profil profil1 = Profil.builder().region("Nouvelle Aquitaine").build();
                Profil profil2 = Profil.builder().region("Nouvelle Aquitaine").build();

                //when 
                when(profilRepository.findAllByRegion(region)).thenReturn(List.of(profil1, profil2));

                //then
                List<Profil> result = profilServiceImpl.getAllProfilsByRegion(region);

                assertNotNull(result);
                assertEquals(profil1.getRegion(), "Nouvelle Aquitaine");
                assertEquals(profil2.getRegion(), "Nouvelle Aquitaine");
                assertNotEquals(profil1.getRegion(), "Region PACA");
        }

}
