package com.devandre.mediumclone.security.service;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * @author Andre on 22/02/2024
 * @project medium-clone
 */
@Component
public class PasswordService {
    private final transient PasswordEncoder encoder = new BCryptPasswordEncoder();

    public String encodePassword(final String rowPassword) {
        return encoder.encode(rowPassword);
    }

    public boolean matchesRowPasswordWithEncodedPassword(final String rowPassword, final String encodedPassword) {
        return encoder.matches(rowPassword, encodedPassword);
    }
}
