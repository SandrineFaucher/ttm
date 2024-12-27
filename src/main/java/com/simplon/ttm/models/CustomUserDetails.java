package com.simplon.ttm.models;

import java.util.Collection;
import java.util.Collections;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.simplon.ttm.dto.LoginDto;

public class CustomUserDetails implements UserDetails {

    private String username;
    private String password;
    private GrantedAuthority authority;

    // Constructeur pour initialiser l'utilisateur à partir du LoginDto
    public CustomUserDetails(User user) {
        this.username = user.getUsername();
        this.password = user.getPassword();
        this.authority = () -> "ROLE_" + user.getRole(); // Assuming 'role' is a String in LoginDto
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(authority); // Retourne une liste contenant l'unique autorité de l'utilisateur
    }

    @Override
    public String getPassword() {
        return password; // Retourne le mot de passe de l'utilisateur
    }

    @Override
    public String getUsername() {
        return username; // Retourne le nom d'utilisateur
    }

    @Override
    public boolean isAccountNonExpired() {
        return true; // L'utilisateur est toujours actif, pas de gestion de date d'expiration dans cet exemple
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; // L'utilisateur n'est pas verrouillé
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // Les identifiants de l'utilisateur ne sont pas expirés
    }

    @Override
    public boolean isEnabled() {
        return true; // L'utilisateur est activé
    }
}