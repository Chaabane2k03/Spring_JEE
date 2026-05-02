package tn.fst.classroom_crb.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "security.rate-limit")
public record RateLimitProperties(
        boolean enabled,
        int maxRequests,
        long windowSeconds
) {
}
