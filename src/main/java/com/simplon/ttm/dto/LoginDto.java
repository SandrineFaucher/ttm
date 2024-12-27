package com.simplon.ttm.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LoginDto {

        @NotEmpty(message = "username must not be empty")
        private String username;

        @NotEmpty(message = "password must not be empty")
        private String password;


}

