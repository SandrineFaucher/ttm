package com.simplon.ttm.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.simplon.ttm.models.Sector;

public interface SectorRepository extends JpaRepository <Sector, Long> {
    List<Sector> findAllById(Iterable<Long> ids);
}
