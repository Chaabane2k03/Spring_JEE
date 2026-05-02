package tn.fst.classroom_crb.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

// Composant géré par Spring (candidat au scanning)
@Component
// Déclare ce filtre comme un bean Spring automatiquement détecté
public class ApiRateLimitingFilter extends OncePerRequestFilter {

    // Configuration du rate limiting (max requests, window time, activation)
    private final RateLimitProperties rateLimitProperties;

    // Stocke le nombre de requêtes par client (IP ou clé utilisateur)
    // ConcurrentHashMap = thread-safe (important car plusieurs requêtes en parallèle)
    private final ConcurrentHashMap<String, WindowCounter> requestCounters = new ConcurrentHashMap<>();

    public ApiRateLimitingFilter(RateLimitProperties rateLimitProperties) {
        this.rateLimitProperties = rateLimitProperties;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        // 1. Si le rate limiting est désactivé → on laisse passer la requête
        if (!rateLimitProperties.enabled()) {
            filterChain.doFilter(request, response);
            return;
        }

        // 2. Filtrage uniquement des endpoints /api/**
        String path = request.getRequestURI().substring(request.getContextPath().length());
        if (!path.startsWith("/api/")) {
            filterChain.doFilter(request, response);
            return;
        }

        // 3. Temps actuel en millisecondes
        long now = System.currentTimeMillis();

        // 4. Durée de la fenêtre de limitation (ex: 60 secondes)
        long windowMs = Math.max(1L, rateLimitProperties.windowSeconds()) * 1000L;

        // 5. Identification du client (IP ou X-Forwarded-For si proxy)
        String clientKey = resolveClientKey(request);

        // 6. Mise à jour atomique du compteur par client
        WindowCounter currentWindow = requestCounters.compute(clientKey, (key, existing) -> {

            // Si pas encore de fenêtre OU fenêtre expirée → reset compteur
            if (existing == null || (now - existing.windowStartMillis) >= windowMs) {
                return new WindowCounter(now, new AtomicInteger(1));
            }

            // Sinon on incrémente le compteur dans la fenêtre actuelle
            existing.requestCount.incrementAndGet();
            return existing;
        });

        // 7. Nombre actuel de requêtes du client
        int currentCount = currentWindow.requestCount.get();

        // 8. Si dépassement du quota → blocage
        if (currentCount > rateLimitProperties.maxRequests()) {

            // Calcul du temps restant avant réessai
            long elapsedInWindow = now - currentWindow.windowStartMillis;
            long retryAfterSeconds =
                    Math.max(1L, (windowMs - elapsedInWindow + 999L) / 1000L);

            // Réponse HTTP 429 (Too Many Requests)
            response.setStatus(429);
            response.setHeader("Retry-After", String.valueOf(retryAfterSeconds));
            response.setContentType("application/json");

            response.getWriter().write("{\"error\":\"Too many requests\"}");
            return;
        }

        // 9. Si tout est OK → on continue la chaîne de filtres
        filterChain.doFilter(request, response);
    }

    /**
     * Détermine l'identité du client :
     * - priorité au header X-Forwarded-For (proxy / load balancer)
     * - sinon IP directe
     */
    private String resolveClientKey(HttpServletRequest request) {
        String forwardedFor = request.getHeader("X-Forwarded-For");

        if (forwardedFor != null && !forwardedFor.isBlank()) {
            return forwardedFor.split(",")[0].trim();
        }

        return request.getRemoteAddr();
    }

    /**
     * Classe interne représentant une fenêtre de temps de rate limiting
     */
    private static final class WindowCounter {

        // Timestamp du début de la fenêtre
        private final long windowStartMillis;

        // Nombre de requêtes dans cette fenêtre
        private final AtomicInteger requestCount;

        private WindowCounter(long windowStartMillis, AtomicInteger requestCount) {
            this.windowStartMillis = windowStartMillis;
            this.requestCount = requestCount;
        }
    }
}
