package com.simplon.ttm.models;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Accompaniement {

    @Id
    @GeneratedValue
    private Long id;

    private String content;

    @ManyToMany(mappedBy = "accompaniements")
    @JsonIgnore // Évite les boucles infinies
    @Builder.Default
    private final List<Profil> profils = new ArrayList<>();

}
