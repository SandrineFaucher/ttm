package com.simplon.ttm.models;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
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
@EntityListeners(AuditingEntityListener.class)
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String username;

    private String email;

    private String password;

    @Enumerated(EnumType.STRING)
    private UserRole role;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Profil profil;

    /**
     * définition table intermédiaire pour valider un match
     */
    @ManyToMany
    @Builder.Default
    @JoinTable(name = "user_match",
    joinColumns = @JoinColumn(name = "godparent_id"),
    inverseJoinColumns = @JoinColumn(name = "leaderproject_id"))
    @JsonIgnore
    private Set<User> user1 = new HashSet<>();
    /**
     * l'attribut mappedBy doit faire référence au nom de l'attribut de l'autre côté de la relation
     */
    @ManyToMany(mappedBy = "user1")
    @Builder.Default
    @JsonIgnore
    private Set<User> user2 = new HashSet<>();

    /**
     * relation bidirectionnelle qui liste les rendez-vous
     */
    @ManyToMany(mappedBy = "participants")
    private List<Appointment> appointments;

    /**
     * Constructeur pour récupérer le user authentifié
     * @param username
     * @param role
     */
    public User(String username, String role) {
    }
}
