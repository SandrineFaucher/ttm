package com.simplon.ttm.config;

import java.io.IOException;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private JwtUtils jwtUtils;
    private UserDetailsService userDetailsService;

    public JwtAuthenticationFilter(JwtUtils jwtUtils, UserDetailsService userDetailsService) {
        this.jwtUtils = jwtUtils;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {

        String token = getTokenFromRequest(request); // Récupération du token depuis l'en-tête Authorization

        if (StringUtils.hasText(token) && jwtUtils.validateToken(token)) {
            String username = jwtUtils.getUsername(token);

            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                    userDetails,
                    null,
                    userDetails.getAuthorities()
            );

            authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        }

        filterChain.doFilter(request, response);
    }

    // Récupération du token à partir de son nom dans le cookie "jwt"
    //    private String getTokenFromRequest(HttpServletRequest request) {
    //        String jwtToken = request.getHeader("jwt");  // Récupère le JWT depuis l'en-tête "jwt"
    //        if (StringUtils.hasText(jwtToken)) {
    //            return jwtToken;  // Si le token existe, le retourne
    //        }
    //        return null;  // Aucun token trouvé
    //    }
    private String getTokenFromRequest(HttpServletRequest request) {
        // Vérifie si le JWT est présent dans les cookies
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if ("JWT".equals(cookie.getName())) {
                    return cookie.getValue(); // Retourne la valeur du cookie
                }
            }
        }

        // Sinon, vérifie si le JWT est présent dans un en-tête spécifique
        String jwtToken = request.getHeader("jwt");
        if (StringUtils.hasText(jwtToken)) {
            return jwtToken; // Si le token existe dans l'en-tête, le retourne
        }

        return null;  // Aucun token trouvé
    }
}
