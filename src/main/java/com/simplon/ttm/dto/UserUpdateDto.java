package com.simplon.ttm.dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserUpdateDto {


    @NotBlank(message = "Le nom d'utilisateur ne peut pas être vide")
    private String username;

    @Column(unique=true)
    @NotBlank(message = "L'email ne peut pas être vide")
    private String email;

}
