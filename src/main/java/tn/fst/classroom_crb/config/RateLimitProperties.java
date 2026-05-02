package tn.fst.classroom_crb.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

// Lie des propriétés externes à une classe de configuration
 @ConfigurationProperties(prefix = "security.rate-limit")
public record RateLimitProperties(
        boolean enabled,
        int maxRequests,
        long windowSeconds
) {
}
