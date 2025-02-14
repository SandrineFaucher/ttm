package com.simplon.ttm.repositories;

import java.util.List;


import org.springframework.data.jpa.repository.JpaRepository;

import com.simplon.ttm.models.Accompaniement;
import com.simplon.ttm.models.Profil;
import com.simplon.ttm.models.Sector;
import com.simplon.ttm.models.UserRole;


public interface ProfilRepository extends JpaRepository <Profil, Long> {


    List<Profil> findAllByUserRole(UserRole userRole);

    Profil findByUserId(Long userId);

    List<Profil> findAllBySectors(Sector sector);

    List<Profil> findAllByAccompaniements(Accompaniement accompaniement);

    List<Profil> findAllByCity(String city);

    List<Profil> findAllByDepartment(String department);

    List<Profil> findAllByRegion(String region);

    
}
