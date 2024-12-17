package com.simplon.ttm.dto;

import com.simplon.ttm.models.UserRole;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
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

    @Column(unique=true)
    @NotEmpty(message = "email must not be empty")
    private String email;

    @NotEmpty(message = "password must not be empty")
    private String password;

    @NotEmpty(message = "confirm password must not be empty")
    private String passwordConfirm;

    @NotNull(message = "role must not be empty")
    private UserRole role;
}
