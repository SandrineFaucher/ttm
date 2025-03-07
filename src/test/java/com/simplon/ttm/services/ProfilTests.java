package com.simplon.ttm.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import java.io.IOException;
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
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

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

        @Mock
        private FileService fileService;

        @InjectMocks
        private ProfilServiceImpl profilServiceImpl;

        @Test
        /**
         * Teste la sauvegarde du profil d'un user inscrit sur le site
         */
        void saveUserProfil() throws IOException {
                // given (Préparation des données)
                LocalDateTime date = LocalDateTime.parse("2024-11-06T00:00:00");

                User user = User.builder()
                        .id(1L)
                        .username("Parain")
                        .password("parain123")
                        .role(UserRole.GODPARENT)
                        .profil(null)  // L'utilisateur ne doit pas avoir de profil
                        .build();

                Sector sector1 = Sector.builder()
                        .id(2L)
                        .content("Informaticien")
                        .build();

                Sector sector2 = Sector.builder()
                        .id(3L)
                        .content("Juridique")
                        .build();

                Accompaniement accompaniement = Accompaniement.builder()
                        .id(6L)
                        .content("Accompagnement")
                        .build();

                // Simuler un fichier image (PNG fictif)
                MockMultipartFile mockImage = new MockMultipartFile(
                        "image", // Nom du champ dans la requête
                        "profil.png", // Nom du fichier
                        "image/png", // Type MIME
                        "fake image content".getBytes() // Contenu fictif
                );

                // Création du DTO sans l'image (puisque retirée du DTO)
                ProfilDto profilDto = ProfilDto.builder()
                        .userId(user.getId()) // Associer l'ID de l'utilisateur
                        .availability("Tous les jeudis")
                        .sectors(List.of(2L, 3L)) // ID des secteurs
                        .accompaniements(List.of(6L))
                        .content("Profil content")
                        .city("Niort")
                        .department("Deux-Sèvres")
                        .region("Nouvelle Aquitaine")
                        .build();

                // Création du profil attendu après sauvegarde
                Profil expectedProfil = Profil.builder()
                        .id(2L)
                        .availability("Tous les jeudis")
                        .sectors(List.of(sector1, sector2))
                        .accompaniements(List.of(accompaniement))
                        .city("Niort")
                        .department("Deux-Sèvres")
                        .region("Nouvelle Aquitaine")
                        .createdAt(date)
                        .user(user)
                        .image("profil_images/profil.png") // Simule une image enregistrée
                        .build();

                // when (Définition des comportements mockés)
                when(userRepository.findById(profilDto.getUserId())).thenReturn(Optional.of(user));

                // Mock de findAllById pour les secteurs et accompagnements
                when(sectorRepository.findAllById(profilDto.getSectors())).thenReturn(List.of(sector1, sector2));
                when(accompaniementRepository.findAllById(profilDto.getAccompaniements())).thenReturn(List.of(accompaniement));

                // Simulation de la sauvegarde d'image qui retourne un chemin ou une URL
                when(fileService.saveFile(any(MultipartFile.class), eq("profil_images"))).thenReturn("profil_images/profil.png");

                // Simulation de la sauvegarde du profil
                when(profilRepository.save(any(Profil.class))).thenReturn(expectedProfil);

                // Simulation de la sauvegarde de l'utilisateur (relation bidirectionnelle)
                when(userRepository.save(any(User.class))).thenReturn(user);

                // Act (Appel du service)
                Profil profilSaved = profilServiceImpl.saveUserProfil(profilDto, mockImage);

                // then (Assertions pour valider le résultat)
                assertNotNull(profilSaved);
                assertEquals("Tous les jeudis", profilSaved.getAvailability());
                assertEquals("Parain", profilSaved.getUser().getUsername());

                // Vérifie que les secteurs sont bien associés
                assertEquals(2, profilSaved.getSectors().size());
                assertTrue(profilSaved.getSectors().contains(sector1));
                assertTrue(profilSaved.getSectors().contains(sector2));

                // Vérifie que les accompagnements sont bien associés
                assertEquals(1, profilSaved.getAccompaniements().size());
                assertTrue(profilSaved.getAccompaniements().contains(accompaniement));

                // Vérifie que l'image a bien été enregistrée avec le bon chemin
                assertEquals("profil_images/profil.png", profilSaved.getImage());
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
                User user = User.builder().id(5L).username("sandrine").build();
                Profil profil = Profil.builder().user(user).build();

                // when
                when(profilRepository.findByUserId(5L)).thenReturn(Optional.of(profil));

                // then
                Optional<Profil> profilTest = profilServiceImpl.getProfilByUserId(5L);

                // Vérifier que le profil est bien présent
                assertTrue(profilTest.isPresent(), "Le profil ne devrait pas être vide");

                // Vérifier les valeurs attendues
                assertEquals("sandrine", profilTest.get().getUser().getUsername());
                assertEquals(5L, profilTest.get().getUser().getId());
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
                Profil profil1 = Profil.builder().sectors(firstProfilSectors).build();
                Profil profil2 = Profil.builder().sectors(secondProfilSectors).build();

                //when
                //mock des profils avec le meme secteur "informatique"
                when(profilRepository.findAllBySectors(secteur1)).thenReturn(List.of(profil1, profil2));

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
                Profil profil1 = Profil.builder().accompaniements(firstProfilAccompaniements).build();
                Profil profil2 = Profil.builder().accompaniements(secondProfilAccompaniements).build();

                //when
                when(profilRepository.findAllByAccompaniements(accompaniement1)).thenReturn(List.of(profil1, profil2));

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
