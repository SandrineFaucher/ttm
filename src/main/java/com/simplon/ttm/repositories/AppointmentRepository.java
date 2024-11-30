package com.simplon.ttm.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.simplon.ttm.models.Appointment;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    List<Appointment> findAppointmentsByParticipantId(Long userId);
}
