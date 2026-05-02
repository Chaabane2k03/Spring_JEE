package tn.fst.classroom_crb.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.SecurityFilterChain;

// Indique que cette classe fournit des beans de configuration Spring (ici : sécurité)
@Configuration
// Active le mapping des propriétés préfixées par `security.rate-limit` vers
// `RateLimitProperties`
// Active le mapping des propriétés externes vers une classe de propriétés
@EnableConfigurationProperties(RateLimitProperties.class)
public class SecurityConfig {

    // Expose le `SecurityFilterChain` comme bean Spring utilisé pour configurer
    // Spring Security
    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http, ApiRateLimitingFilter apiRateLimitingFilter)
            throws Exception {

        // Désactive la protection CSRF (utile pour les APIs REST stateless)
        http.csrf(AbstractHttpConfigurer::disable)

                // Autorise l'affichage dans les frames uniquement pour le même domaine
                .headers(headers -> headers.frameOptions(frame -> frame.sameOrigin()))

                // Autorise toutes les requêtes sans authentification (API ouverte)
                .authorizeHttpRequests(auth -> auth.anyRequest().permitAll())

                // Ajoute un filtre de rate limiting AVANT le filtre d'authentification Spring Security
                // Objectif : limiter le nombre de requêtes par utilisateur/IP pour éviter abuse ou spam
                .addFilterBefore(apiRateLimitingFilter, UsernamePasswordAuthenticationFilter.class);

        // Construit et retourne la chaîne de filtres de sécurité
        return http.build();
    }
}
