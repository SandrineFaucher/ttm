package com.simplon.ttm.services;

import java.io.IOException;
import java.util.Optional;

import org.springframework.web.multipart.MultipartFile;

import com.simplon.ttm.dto.ProfilDto;
import com.simplon.ttm.dto.UpdateProfilDto;
import com.simplon.ttm.models.Profil;


public interface ProfilService {

    Profil saveUserProfil(ProfilDto profilDto, MultipartFile imageUrl) throws IOException;

    Optional <Profil> getProfilByUserId(Long userId);

    Profil updateUserProfil(Optional<Profil> existingProfil, UpdateProfilDto updateProfilDto, MultipartFile image) throws IOException;
}

