package com.devandre.mediumclone.security.jwt.config;

import com.devandre.mediumclone.security.jwt.TokenPrincipal;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.server.authentication.AuthenticationWebFilter;
import org.springframework.security.web.server.authentication.ServerAuthenticationConverter;
import reactor.core.publisher.Mono;

import java.util.Date;
import java.util.List;

/**
 * @author Andre on 20/02/2024
 * @project medium-clone
 */
@Configuration
@EnableConfigurationProperties(JwtProperties.class)
public class JwtConfig {

    /**
     * This method is used to create a ServerAuthenticationConverter bean.
     * The ServerAuthenticationConverter is responsible for converting a ServerWebExchange into an Authentication object.
     * In this case, it extracts the JWT token from the Authorization header of the request.
     * If the Authorization header is not present or empty, it returns an empty Mono.
     * Otherwise, it creates a UsernamePasswordAuthenticationToken with the extracted token as both the principal and credentials.
     *
     * @param tokenExtractor The TokenExtractor to use for extracting the JWT token from the Authorization header.
     * @return A ServerAuthenticationConverter that extracts the JWT token from the Authorization header of the request.
     */
    @Bean
    ServerAuthenticationConverter jwtServerAuthenticationConverter(final TokenExtractor tokenExtractor) {
        return ex -> Mono.justOrEmpty(ex)
                .flatMap(exchange -> {
                    final var headers = ex.getRequest()
                            .getHeaders()
                            .get("Authorization");
                    if (headers == null || headers.isEmpty()) {
                        return Mono.empty();
                    }
                    final var authHeader = headers.get(0);
                    final var token = tokenExtractor.extractToken(authHeader);
                    return Mono.just(new UsernamePasswordAuthenticationToken(
                            token,
                            token
                    ));
                });
    }

    /**
     * This method is used to create a ReactiveAuthenticationManager bean.
     * The ReactiveAuthenticationManager is responsible for authenticating the JWT token.
     * It validates the token, extracts the user ID from it, and creates a new UsernamePasswordAuthenticationToken with
     * the user ID and token as the principal and credentials, respectively.
     *
     * @param tokenService The JwtSigner to use for validating the JWT token.
     * @return A ReactiveAuthenticationManager that authenticates the JWT token.
     */
    @Bean
    ReactiveAuthenticationManager jwtAuthenticationManager(final JwtSigner tokenService) {
        return authentication -> Mono.justOrEmpty(authentication)
                .map(auth -> {
                    final String token = (String) auth.getCredentials();
                    final Jws<Claims> jws = tokenService.validate(token);
                    final String userId = jws.getPayload()
                            .getSubject();
                    final Date expiration = jws.getPayload()
                            .getExpiration();
                    final TokenPrincipal tokenPrincipal = new TokenPrincipal(
                            userId,
                            token,
                            expiration
                    );
                    return new UsernamePasswordAuthenticationToken(
                            tokenPrincipal,
                            token,
                            List.of(new SimpleGrantedAuthority("ROLE_USER"))
                    );
                });
    }

    /**
     * This method is used to create an AuthenticationWebFilter bean.
     * The AuthenticationWebFilter is responsible for authenticating web requests.
     * It uses the provided ReactiveAuthenticationManager for authentication and the
     * ServerAuthenticationConverter for converting the ServerWebExchange into an Authentication object.
     *
     * @param manager The ReactiveAuthenticationManager to use for authentication.
     * @param converter The ServerAuthenticationConverter to use for converting the ServerWebExchange into an Authentication object.
     * @return An AuthenticationWebFilter that authenticates web requests.
     */
    @Bean
    AuthenticationWebFilter authenticationFilter(final ReactiveAuthenticationManager manager,
                                                 final ServerAuthenticationConverter converter) {
        final AuthenticationWebFilter authenticationWebFilter = new AuthenticationWebFilter(manager);
        authenticationWebFilter.setServerAuthenticationConverter(converter);
        return authenticationWebFilter;
    }

}
