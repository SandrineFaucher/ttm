package com.simplon.ttm.services.impl;

import java.util.List;
import java.util.Optional;

import com.simplon.ttm.models.Appointment;
import com.simplon.ttm.models.User;
import com.simplon.ttm.repositories.AppointmentRepository;
import com.simplon.ttm.repositories.UserRepository;
import com.simplon.ttm.services.AppointmentService;

import jakarta.persistence.EntityNotFoundException;

public class AppointmentServiceImpl implements AppointmentService {
    private AppointmentRepository appointmentRepository;
    private UserRepository userRepository;

    public AppointmentServiceImpl(UserRepository userRepository, AppointmentRepository appointmentRepository) {
        this.userRepository = userRepository;
        this.appointmentRepository = appointmentRepository;
    }

    /**
     * save an appointment
     * @param appointment
     * @return appointment
     */
    public Appointment save(Appointment appointment) {
        // Vérifier que tous les participants existent
        appointment.getParticipants().forEach(user -> {
            Optional<User> existingUser = userRepository.findById(user.getId());
            if (existingUser.isEmpty()) {
                throw new IllegalArgumentException("User not found with ID: " + user.getId());
            }
        });
        // Sauvegarde de l'appointment
        return appointmentRepository.save(appointment);
    }

    /**
     *
     * @param userId
     * @return list of appointment
     */
    public List<Appointment> getAppointmentsByParticipantId(Long userId) {
        if (userId == null) {
            throw new IllegalArgumentException("userId cannot be null");
        }
        List<Appointment> appointments = appointmentRepository.findAppointmentsByParticipantId(userId);
        if (appointments.isEmpty()) {
            throw new EntityNotFoundException("No appointment for userId : " + userId);
        }
        return appointments;
    }
}

