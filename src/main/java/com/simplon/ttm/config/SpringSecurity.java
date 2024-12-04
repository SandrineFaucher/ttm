package com.simplon.ttm.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity

public class SpringSecurity {

        private final UserDetailsService userDetailsService;

        private PasswordEncoder passwordEncoder;

        @Autowired
        public SpringSecurity(UserDetailsService userDetailsService, PasswordEncoder passwordEncoder) {
            this.userDetailsService = userDetailsService;
            this.passwordEncoder = passwordEncoder;
        }


        // accessibilités via l'authentification avec une filter chain
        @Bean
        public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
            return http
                    .csrf(csrf -> csrf.disable())
                    .authorizeHttpRequests(requests -> {
                        requests
                                .requestMatchers("/register/**").permitAll() //.hasRole("ADMIN")
                                .requestMatchers("/login").permitAll() // Accessible à tous les utilisateuurs
                                .requestMatchers("/admin/**").hasRole("ADMIN") // Nécessite le rôle ADMIN
                                .requestMatchers("/godparent/**").hasRole("GODPARENT") // Nécessite le rôle USER
                                .requestMatchers("/leaderproject/**").hasRole("LEADERPROJECT") // Nécessite le rôle USER
                                .anyRequest().authenticated(); // Toute autre requête doit être authentifiée
                    })
                    .formLogin(form -> form
                            // .loginPage("/login") // Page de login personnalisée
                            .permitAll() // Permet à tous d'y accéder
                    )
                    .logout(logout -> logout
                            .logoutSuccessUrl("/") // Redirige vers la page d'accueil après déconnexion
                            .permitAll() // Autorise la déconnexion pour tous
                    )
                    .build();
        }

        @Autowired
        public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder);
        }
}


