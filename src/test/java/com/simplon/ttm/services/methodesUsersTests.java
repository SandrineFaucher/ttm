package com.simplon.ttm.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.simplon.ttm.dto.RegisterDto;
import com.simplon.ttm.models.User;
import com.simplon.ttm.models.UserRole;
import com.simplon.ttm.repositories.UserRepository;
import com.simplon.ttm.services.impl.UserServiceImpl;

public class methodesUsersTests {

    @ExtendWith(MockitoExtension.class)
       
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userServiceImpl;

    @Test
    void saveGodparent(){
        //given
        LocalDate date = LocalDate.parse("2024-11-06");
        User godparent = User.builder()
                .id(1L)
                .username("Parain")
                .password("parain123")
                .role(UserRole.GODPARENT)
                .creationDate(date)
                .build();
        RegisterDto godparentDto = RegisterDto.builder()
            .username("Parain")
            .password("parain123")
            .role(UserRole.GODPARENT)
            .build();

        //when
        when(userRepository.save(any(User.class))).thenReturn(godparent);
        

        //then
        User user = userServiceImpl.saveGodparent(godparentDto);
        assertEquals(godparent, user);
    }
}


