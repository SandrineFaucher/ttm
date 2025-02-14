package com.simplon.ttm.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.simplon.ttm.models.Accompaniement;
import com.simplon.ttm.models.Sector;

@Repository
public interface AccompaniementRepository extends JpaRepository<Accompaniement, Long> {
    List<Accompaniement> findAllById(Iterable<Long> ids);
}
