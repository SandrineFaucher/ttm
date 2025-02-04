package com.simplon.ttm.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.simplon.ttm.models.Accompaniement;

@Repository
public interface AccompaniementRepository extends JpaRepository<Accompaniement, Long> {

}
