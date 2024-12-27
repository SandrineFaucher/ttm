package com.simplon.ttm.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import jakarta.servlet.http.HttpServletResponse;

@Configuration
@EnableWebSecurity

public class SpringSecurity {

        private PasswordEncoder passwordEncoder;
        private final UserDetailsService userDetailsService;

    @Autowired
    public SpringSecurity(UserDetailsService userDetailsService, PasswordEncoder passwordEncoder) {
        this.userDetailsService = userDetailsService;
        this.passwordEncoder = passwordEncoder;
    }
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

        // accessibilités via l'authentification avec une filter chain
        @Bean
        public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
            return http

                    .csrf(csrf -> csrf.disable())
                    .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                    .authorizeHttpRequests(requests -> {
                        requests
                                .requestMatchers("/admin/register/**").permitAll() //.hasRole("ADMIN")
                                .requestMatchers("/login", "/users/**").permitAll() // Accessible à tous les utilisateuurs
                                .anyRequest().authenticated(); // Toute autre requête doit être authentifiée
                    })
                    .formLogin(form -> form
                            .loginPage("/login") // Spécifie une page de connexion personnalisée (React ou autre)
                            .successHandler((request, response, authentication) -> {
                                // Retourne un message JSON au lieu de rediriger
                                response.setStatus(HttpServletResponse.SC_OK);
                                response.getWriter().write("{\"message\":\"Login successful\"}");
                            })
                            .failureHandler((request, response, exception) -> {
                                // Retourne un message d'échec JSON au lieu de rediriger
                                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                                response.getWriter().write("{\"error\":\"Invalid credentials\"}");
                            })
                            .permitAll()
                    )
                    .logout(logout -> logout
                            .logoutSuccessUrl("/") // Redirige vers la page d'accueil après déconnexion
                            .permitAll() // Autorise la déconnexion pour tous
                    )
                    .build();
        }
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:5173")); // Origines autorisées (React)
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS")); // Méthodes HTTP autorisées
        configuration.setAllowedHeaders(List.of("Authorization", "Content-Type", "Accept")); // En-têtes autorisés
        configuration.setAllowCredentials(true); // Autoriser l'envoi de cookies ou de credentials
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration); // Applique la configuration à toutes les routes
        return source;
    }

        @Autowired
        public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder);
        }


}


