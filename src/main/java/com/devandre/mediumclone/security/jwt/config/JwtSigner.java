package com.devandre.mediumclone.security.jwt.config;

import com.devandre.mediumclone.security.provider.UserTokenProvider;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.security.KeyPair;
import java.util.Base64;
import java.util.Date;

/**
 * @author Andre on 20/02/2024
 * @project medium-clone
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class JwtSigner implements UserTokenProvider {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private long expiration;

    private final JwtProperties jwtProperties;

    public Jws<Claims> validate(final String jwt) {
        return Jwts.parser()
                .verifyWith(getDeserializedKey(secret))
                .build()
                .parseSignedClaims(jwt);
    }

    public String generateToken(final String userId) {

        Date ex = new Date(System.currentTimeMillis() + expiration * 60 * 1000);

        return Jwts.builder()
                .signWith(getDeserializedKey(secret))
                .subject(userId)
                // set expiration time to 10 minutes
                .claim("expTime", ex)
                .expiration(ex)
                .compact();
    }

    public Date getExpTime(final String userId) {
        return Jwts.parser()
                .verifyWith(getDeserializedKey(secret))
                .build()
                .parseSignedClaims(generateToken(userId))
                .getPayload()
                .getExpiration();
    }

    @Override
    public String getToken(String userId) {
        return generateToken(userId);
    }

    @Override
    public Date getExpirationTime(String userId) {
        return getExpTime(userId);
    }

    private SecretKey getDeserializedKey(final String secret) {
        byte[] decodedKey = Decoders.BASE64.decode(secret);
        return new SecretKeySpec(
                decodedKey,
                0,
                decodedKey.length,
                "HmacSHA256"
        );
    }
}
