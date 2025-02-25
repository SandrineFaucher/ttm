package com.simplon.ttm.services.impl;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.simplon.ttm.dto.ProfilDto;
import com.simplon.ttm.dto.UpdateProfilDto;
import com.simplon.ttm.models.Accompaniement;
import com.simplon.ttm.models.Profil;
import com.simplon.ttm.models.Sector;
import com.simplon.ttm.models.User;
import com.simplon.ttm.models.UserRole;
import com.simplon.ttm.repositories.AccompaniementRepository;
import com.simplon.ttm.repositories.ProfilRepository;
import com.simplon.ttm.repositories.SectorRepository;
import com.simplon.ttm.repositories.UserRepository;
import com.simplon.ttm.services.FileService;
import com.simplon.ttm.services.ProfilService;

import jakarta.transaction.Transactional;

@Service
public class ProfilServiceImpl implements ProfilService {

    private  UserRepository userRepository;
    private   ProfilRepository profilRepository;

    private  SectorRepository sectorRepository;

    private  AccompaniementRepository accompaniementRepository;

    private FileService fileService;


    @Autowired
    public ProfilServiceImpl(UserRepository userRepository,
                             ProfilRepository profilRepository,
                             SectorRepository sectorRepository,
                             AccompaniementRepository accompaniementRepository,
                             FileService fileService) {
        this.userRepository = userRepository;
        this.profilRepository = profilRepository;
        this.sectorRepository = sectorRepository;
        this.accompaniementRepository = accompaniementRepository;
        this.fileService = fileService;
    }

    @Transactional
    public Profil saveUserProfil(ProfilDto profilDto, MultipartFile image) throws IOException {
        // Récupère le user à associer au profil
        User user = userRepository.findById(profilDto.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        // Vérifier si un profil existe déjà pour cet utilisateur
        if (user.getProfil() != null) {
            throw new IllegalArgumentException("L'utilisateur a déjà un profil");
        }

        // Crée un nouvel objet Profil à partir du ProfilDto
        Profil profil = new Profil();
        profil.setAvailability(profilDto.getAvailability());
        profil.setContent(profilDto.getContent());
        profil.setCity(profilDto.getCity());
        profil.setDepartment(profilDto.getDepartment());
        profil.setRegion(profilDto.getRegion());

        // Gérer l'image si elle est présente
        if (image != null && !image.isEmpty()) {
            String imageUrl = fileService.saveFile(image, "profil_images"); // Sauvegarde et récupération du chemin
            profil.setImage(imageUrl);
            System.out.println("Image enregistrée : " + imageUrl);
        } else {
            profil.setImage(null);
        }


        // Récupère les secteurs sélectionnés par l'utilisateur
        List<Sector> sectors = Optional.ofNullable(profilDto.getSectors())
                .filter(ids -> !ids.isEmpty())  // Évite une requête inutile si la liste est vide
                .map(sectorRepository::findAllById) // Récupère tous les secteurs par leurs ID
                .orElseThrow(() -> new IllegalArgumentException("Un ou plusieurs secteurs sélectionnés n'existent pas"));

        // Vérifie que tous les secteurs demandés ont bien été trouvés
        if (sectors.size() != profilDto.getSectors().size()) {
            throw new IllegalArgumentException("Un ou plusieurs secteurs sélectionnés sont invalides");
        }

        // Associe les secteurs au profil
        profil.setSectors(sectors);

        // Récupère les accompagnements sélectionnés par l'utilisateur
        List<Accompaniement> accompaniements = Optional.ofNullable(profilDto.getAccompaniements())
                .filter(ids -> !ids.isEmpty())  // Évite une requête inutile si vide
                .map(accompaniementRepository::findAllById) // Récupère tous les accompagnements par leurs ID
                .orElseThrow(() -> new IllegalArgumentException("Un ou plusieurs accompagnements sélectionnés n'existent pas"));

        // Vérifie que tous les accompagnements demandés ont bien été trouvés
        if (accompaniements.size() != profilDto.getAccompaniements().size()) {
            throw new IllegalArgumentException("Un ou plusieurs accompagnements sélectionnés n'existent pas");
        }

        // Associe les accompagnements au profil
        profil.setAccompaniements(accompaniements);

        // Associer le profil à l'utilisateur
        profil.setUser(user);

        // Sauvegarde d'abord le profil
        profil = profilRepository.save(profil);

        // Met à jour l'utilisateur avec le profil
        user.setProfil(profil);

        // Sauvegarde l'utilisateur pour établir la relation bidirectionnelle
        userRepository.save(user);

        return profil;
    }


    @Transactional
    public Profil updateUserProfil(Optional<Profil> optionalProfil, UpdateProfilDto updateProfilDto, MultipartFile image) throws IOException {
        // Vérifier si le profil existe
        Profil existingProfil = optionalProfil.orElseThrow(() -> new IllegalArgumentException("Profil not found"));

        // Mettre à jour les champs du profil
        existingProfil.setAvailability(updateProfilDto.getAvailability());
        existingProfil.setContent(updateProfilDto.getContent());
        existingProfil.setCity(updateProfilDto.getCity());
        existingProfil.setDepartment(updateProfilDto.getDepartment());
        existingProfil.setRegion(updateProfilDto.getRegion());

        // Gérer l'image si elle est fournie
        if (image != null && !image.isEmpty()) {
            // Supprime l'ancienne image si elle existe
            if (existingProfil.getImage() != null) {
                fileService.deleteFile(existingProfil.getImage());
            }
            // Sauvegarde la nouvelle image
            String imageUrl = fileService.saveFile(image, "profil_images");
            existingProfil.setImage(imageUrl);
        }

        // Mettre à jour les secteurs sélectionnés
        if (updateProfilDto.getSectors() != null) {
            List<Sector> sectors = sectorRepository.findAllById(updateProfilDto.getSectors());
            if (sectors.size() != updateProfilDto.getSectors().size()) {
                throw new IllegalArgumentException("Un ou plusieurs secteurs sélectionnés sont invalides");
            }
            existingProfil.setSectors(sectors);
        }

        // Mettre à jour les accompagnements sélectionnés
        if (updateProfilDto.getAccompaniements() != null) {
            List<Accompaniement> accompaniements = accompaniementRepository.findAllById(updateProfilDto.getAccompaniements());
            if (accompaniements.size() != updateProfilDto.getAccompaniements().size()) {
                throw new IllegalArgumentException("Un ou plusieurs accompagnements sélectionnés sont invalides");
            }
            existingProfil.setAccompaniements(accompaniements);
        }

        // Sauvegarde du profil mis à jour
        return profilRepository.save(existingProfil);
    }
    public List<Profil> getAllProfilsByRole(UserRole userRole) {
        
        List<Profil> profils = profilRepository.findAllByUserRole(userRole);
        return  profils;
    }

    public Optional<Profil> getProfilByUserId(Long userId) {
        return profilRepository.findByUserId(userId);
    }

    public List<Profil> getAllProfilBySector(Sector sector) {
        List<Profil> profils = profilRepository.findAllBySectors(sector);
        return profils;
    }

    public List<Profil> getAllProfilByAccompaniement(Accompaniement accompaniement) {
        List<Profil> profils = profilRepository.findAllByAccompaniements(accompaniement);
        return profils;
    }

    public List<Profil> getAllProfilsByCity(String city) {
        List<Profil> profils = profilRepository.findAllByCity(city);
        return profils;
    }

    public List<Profil> getAllProfilsByDepartment(String department) {
        List<Profil> profils = profilRepository.findAllByDepartment(department);
        return profils; 
    }

    public List<Profil> getAllProfilsByRegion(String region) {
        List<Profil> profils = profilRepository.findAllByRegion(region);
        return profils;
    }
}
