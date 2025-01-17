package com.simplon.ttm.services.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


import com.simplon.ttm.dto.RegisterDto;
import com.simplon.ttm.dto.UpdatePasswordDto;
import com.simplon.ttm.dto.UserUpdateDto;
import com.simplon.ttm.models.User;
import com.simplon.ttm.models.UserRole;
import com.simplon.ttm.repositories.UserRepository;
import com.simplon.ttm.services.UserService;

@Service
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder){

        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     *
     * @param user à enregistrer
     * @return la sauvegarde du user
     */
    public User saveUserWithRole(RegisterDto user) {
        // Validation et conversion du champ role en UserRole
        UserRole role;
        try {
            role = UserRole.valueOf(String.valueOf(user.getRole()).toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid role: " + user.getRole());
        }

        // Création et sauvegarde de l'utilisateur
        User newUser = User.builder()
                .username(user.getUsername())
                .email(user.getEmail())
                .password(passwordEncoder.encode(user.getPassword()))
                .role(role)
                .build();
        return userRepository.save(newUser);
    }
    @Override
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    /**
     *
     * @param username de l'utilisateur à mettre à jour
     * @param userUpdateDto données du formulaire de modification
     * @return l'utilisateur modifié
     */
    public User updateUserByUsername(String username, UserUpdateDto userUpdateDto) {
        // Récupère l'utilisateur par son username
        User existingUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

        // Étape 3 : Mise à jour des champs autorisés
        existingUser.setUsername(userUpdateDto.getUsername());
        existingUser.setEmail(userUpdateDto.getEmail());

        System.out.println("Données mises à jour : Username = " + existingUser.getUsername() + ", Email = " + existingUser.getEmail());

        // Étape 4 : Sauvegarde dans la base de données
        User updatedUser = userRepository.save(existingUser);

        System.out.println("Utilisateur mis à jour et sauvegardé : " + updatedUser);

        return updatedUser;
    }
    public User updatePasswordByUsername(String username, UpdatePasswordDto updatePasswordDto){
        // Je récupère l'utilisateur par son username
        User existingUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

        // Vérification que l'ancien mot de passe est correct
        if (!passwordEncoder.matches(updatePasswordDto.getOldPassword(), existingUser.getPassword())) {
            throw new RuntimeException("L'ancien mot de passe est incorrect");
        }
        // je vérifie que nouveau mot de passe et confirme mote de passe sont identiques
        if (!updatePasswordDto.getNewPassword().equals(updatePasswordDto.getConfirmPassword())) {
            throw new RuntimeException("Les mots de passe ne correspondent pas");
        }
        // Si c'est ok je hash le mot de passe pour l'enregistrer
        String encodedPassword = passwordEncoder.encode(updatePasswordDto.getNewPassword());
        existingUser.setPassword(encodedPassword);
        // Enregistrement du nouveau mot de passe
        return userRepository.save(existingUser);

    }
    /**
     * Méthode qui permet d'afficher les users avec le bon role au user connecté
     * @param username
     * @return
     */
    public List<User> getUsersVisibleToCurrentUser(String username) {
        // Récupére l'utilisateur connecté
        User currentUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

        // Détermine le rôle visible pour cet utilisateur
        UserRole visibleRole;
        if (currentUser.getRole() == UserRole.GODPARENT) {
            visibleRole = UserRole.LEADERPROJECT;
        } else if (currentUser.getRole() == UserRole.LEADERPROJECT) {
            visibleRole = UserRole.GODPARENT;
        } else {
            throw new AccessDeniedException("Role not allowed to view users");
        }
        // Retourner les utilisateurs ayant le rôle visible
        return userRepository.findByRole(visibleRole);
    }
    
    public Optional<User> getUserByUsername(String username) {
            return userRepository.findByUsername(username);
        }

    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }


    /**
     * Service qui permet de sauver un match entre deux users
     * @param userId1
     * @param userId2
     * @return boolean
     */
    public boolean saveMatch(long userId1, long userId2) {
        Optional <User> userA = userRepository.findById(userId1);
        Optional<User> userB = userRepository.findById(userId2);

        if(userA.isPresent() && userB.isPresent()){
            User user1 = userA.get();
            User user2 = userB.get();

            user1.getUser1().add(user2);
            user2.getUser2().add(user1);
    
            userRepository.save(user1);
            userRepository.save(user2);
            return true;
        }
        return false;
    }
}

    



