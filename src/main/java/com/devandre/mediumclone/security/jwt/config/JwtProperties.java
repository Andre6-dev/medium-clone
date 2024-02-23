package com.devandre.mediumclone.security.jwt.config;

import lombok.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.ConstructorBinding;

/**
 * @author Andre on 20/02/2024
 * @project medium-clone
 */
@Value
@ConfigurationProperties(prefix = "jwt")
public class JwtProperties {
    int sessionTime;
}
