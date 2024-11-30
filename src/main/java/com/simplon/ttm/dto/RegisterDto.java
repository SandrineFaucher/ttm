package com.simplon.ttm.dto;

import com.simplon.ttm.models.UserRole;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class RegisterDto {
    
    @NotEmpty(message = "username must not be empty")
    private String username;

    @NotEmpty(message = "password must not be empty")
    private String password;

    @NotEmpty(message = "role must not be empty")
    private UserRole role;
}
