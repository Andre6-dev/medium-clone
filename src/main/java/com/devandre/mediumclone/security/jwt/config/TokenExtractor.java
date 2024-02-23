package com.devandre.mediumclone.security.jwt.config;

import com.devandre.mediumclone.exception.InvalidRequestException;
import org.springframework.stereotype.Component;

/**
 * @author Andre on 20/02/2024
 * @project medium-clone
 */
@Component
public class TokenExtractor {

    public String extractToken(final String authorizationHeader) {
        if (!authorizationHeader.startsWith("Bearer ")) {
            throw new InvalidRequestException("Authorization Header", "has no `Bearer` prefix");
        }

        return authorizationHeader.substring("Bearer ".length());
    }
}
