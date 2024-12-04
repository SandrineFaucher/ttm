package com.simplon.ttm.models;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue
    private Long id;

    private String username;

    private String email;

    private String password;

    @Enumerated(EnumType.STRING)
    private UserRole role;

    private LocalDate creationDate;

    @OneToOne(mappedBy = "user")
    private Profil profil;

    /**
     * définition table intermédiaire pour valider un match
     */
    @ManyToMany
    @JoinTable(name = "user_match",
    joinColumns = @JoinColumn(name = "godparent_id"),
    inverseJoinColumns = @JoinColumn(name = "projectLeader_id"))
    private Set<User> user1 = new HashSet<>();
    /**
     * l'attribut mappedBy doit faire référence au nom de l'attribut de l'autre côté de la relation
     */
    @ManyToMany(mappedBy = "user1")
    private Set<User> user2 = new HashSet<>();

    /**
     * relation bidirectionnelle qui liste les rendez-vous
     */
    @ManyToMany(mappedBy = "participants")
    private List<Appointment> appointments;
}
