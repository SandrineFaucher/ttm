package com.simplon.ttm.services;

import java.io.IOException;

import org.springframework.web.multipart.MultipartFile;

import com.simplon.ttm.dto.ProfilDto;
import com.simplon.ttm.models.Profil;


public interface ProfilService {

    Profil saveUserProfil(ProfilDto profilDto, MultipartFile imageUrl) throws IOException;

}
