package com.simplon.ttm.services;

import com.simplon.ttm.dto.ProfilDto;
import com.simplon.ttm.models.Profil;


public interface ProfilService {

    Profil saveUserProfil(ProfilDto profilDto);

}
