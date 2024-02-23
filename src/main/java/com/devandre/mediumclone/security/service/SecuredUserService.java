package com.devandre.mediumclone.security.service;

import com.devandre.mediumclone.dto.request.UserAuthenticationRequest;
import com.devandre.mediumclone.dto.request.UserRegistrationRequest;
import com.devandre.mediumclone.dto.response.UserResponse;
import com.devandre.mediumclone.exception.InvalidRequestException;
import com.devandre.mediumclone.persistence.entity.User;
import com.devandre.mediumclone.persistence.repository.UserRepository;
import com.devandre.mediumclone.security.provider.UserTokenProvider;
import io.micrometer.common.util.StringUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.UUID;

/**
 * @author Andre on 22/02/2024
 * @project medium-clone
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class SecuredUserService {

    private static final String ALREADY_IN_USE = "Username already in use";

    private final UserRepository userRepository;

    private final PasswordService passwordService;

    private final UserTokenProvider tokenProvider;

    public Mono<UserResponse> login(final Mono<UserAuthenticationRequest> request) {
        log.info("Logging in user");
        return request.flatMap(m -> {
            final var email = m.getEmail();
            final var password = m.getPassword();
            return userRepository.findByEmailOrFail(email)
                    .flatMap(user -> {
                        final String encodedPassword = user.getEncodedPassword();
                        return passwordService.matchesRowPasswordWithEncodedPassword(password, encodedPassword)
                                ? Mono.just(user)
                                : Mono.error(new InvalidRequestException("Password", "invalid"));
                    }).map(this::createAuthenticationResponse);
        });
    }

    public Mono<UserResponse> signUp(final UserRegistrationRequest request) {
        log.info("Signing up user");
        return userRepository.existsByEmail(request.getEmail())
                .flatMap(emailExists -> {
                    if (emailExists) {
                        return Mono.error(new InvalidRequestException("Email", "Email already in use"));
                    }
                    return userRepository.existsByUsername(request.getUsername());
                })
                .flatMap(usernameExists -> {
                    if (usernameExists) {
                        return Mono.error(new InvalidRequestException("Username", ALREADY_IN_USE));
                    }
                    final String encodedPassword = passwordService.encodePassword(request.getPassword());
                    final String id = UUID.randomUUID().toString();
                    final User userToCreate = request.toUser(encodedPassword, id);
                    log.info("User details: {}", userToCreate.toString());
                    return userRepository.save(userToCreate)
                            .map(this::createAuthenticationResponse);
                });
    }

    private UserResponse createAuthenticationResponse(final User user) {
        log.info("User {} has been authenticated", user.getUsername());
        final var token = tokenProvider.getToken(user.getId());
        final var expirationDate = tokenProvider.getExpirationTime(token);
        return UserResponse.fromUserAndToken(user, token, expirationDate);
    }

}
