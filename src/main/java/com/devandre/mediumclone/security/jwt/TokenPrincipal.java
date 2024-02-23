package com.devandre.mediumclone.security.jwt;

import java.util.Date;

/**
 * @author Andre on 19/02/2024
 * @project medium-clone
 */
public record TokenPrincipal(String userId, String token, Date expirationDate) {
}
