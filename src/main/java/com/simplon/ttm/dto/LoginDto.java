package com.simplon.ttm.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginDto {

        @NotEmpty(message = "username must not be empty")
        private String username;

        @NotEmpty(message = "password must not be empty")
        private String password;

    }

