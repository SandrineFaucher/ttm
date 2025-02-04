package com.simplon.ttm.services;

import java.util.List;
import java.util.Optional;

import com.simplon.ttm.dto.AccompaniementDto;
import com.simplon.ttm.models.Accompaniement;

public interface AccompaniementService {
    void saveAccompaniement(AccompaniementDto accompaniementDto);

    Optional<Accompaniement> getById(Long id);

    Accompaniement updateAccompaniement(Accompaniement accompaniement, AccompaniementDto accompaniementDto);

    void deleteAccompaniement(Long id);

    List<Accompaniement> findAll();
}
