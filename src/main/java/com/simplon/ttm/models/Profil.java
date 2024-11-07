package com.simplon.ttm.models;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Builder.Default;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class Profil {

    @Id
    @GeneratedValue
    private Long id;

    private String availability;

    @OneToMany
    @Default
    private List<Sector> sector = new ArrayList<>();

    @OneToMany
    @Default
    private List<Accompaniement> accompaniement = new ArrayList<>();

    private String content;

    private String city;

    private String department;

    private String region;

    private String image;

    private LocalDate creationDate;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

}
