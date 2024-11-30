package com.simplon.ttm.services;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.simplon.ttm.models.Appointment;
import com.simplon.ttm.models.User;
import com.simplon.ttm.models.UserRole;
import com.simplon.ttm.repositories.AppointmentRepository;
import com.simplon.ttm.repositories.UserRepository;
import com.simplon.ttm.services.impl.AppointmentServiceImpl;

public class AppointmentTests {

    @ExtendWith(MockitoExtension.class)

    @Mock
    private AppointmentRepository appointmentRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private AppointmentServiceImpl appointmentServiceImpl;

    /**
     * teste la sauvegarde d'un rendez-vous entre deux users
     */
    @Test
    void saveAppointment() {
        //given
        User user1 = User.builder()
                .id(1L)
                .username("Sandrine")
                .role(UserRole.GODPARENT)
                .build();
        User user2 = User.builder()
                .id(2L)
                .username("Lola")
                .role(UserRole.LEADERPROJECT)
                .build();
        List<User> participants = List.of(user1, user2);
        Appointment appointment = Appointment.builder()
                .id(2L)
                .hour(LocalTime.of(14, 30))
                .date(LocalDate.of(2024, 11, 24))
                .location("Niort")
                .participants(participants)
                .build();

        //when
        when(appointmentRepository.save(any(Appointment.class))).thenReturn(appointment);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user1));
        when(userRepository.findById(2L)).thenReturn(Optional.of(user2));

        //then
        Appointment appointmentSaved = appointmentServiceImpl.save(appointment);

        assertNotNull(appointmentSaved);
        assertEquals(2L, appointmentSaved.getId());
        assertEquals("Niort", appointmentSaved.getLocation());
        assertEquals(2, appointmentSaved.getParticipants().size());
        assertEquals("Sandrine", appointmentSaved.getParticipants().get(0).getUsername());
        assertEquals("Lola", appointmentSaved.getParticipants().get(1).getUsername());
    }

    @Test
    void getAppointmentByParticipantId() {
        // given
        User user1 = User.builder()
                .id(1L)
                .username("Sandrine")
                .role(UserRole.GODPARENT)
                .build();
        User user2 = User.builder()
                .id(2L)
                .username("Lola")
                .role(UserRole.LEADERPROJECT)
                .build();
        List<User> participants = List.of(user1, user2);

        Appointment appointment = Appointment.builder()
                .id(2L)
                .hour(LocalTime.of(14, 30))
                .date(LocalDate.of(2024, 11, 24))
                .location("Niort")
                .participants(participants)
                .build();
        //when
        when(appointmentRepository.findAppointmentsByParticipantId(user2.getId()))
                .thenReturn(List.of(appointment));
        //then
        List<Appointment> result = appointmentServiceImpl.getAppointmentsByParticipantId(user2.getId());

        // then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Sandrine", result.get(0).getParticipants().get(0).getUsername());
        assertEquals(appointment, result.get(0));

    }
}
