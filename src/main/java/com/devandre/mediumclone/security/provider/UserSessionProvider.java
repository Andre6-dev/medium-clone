package com.devandre.mediumclone.security.provider;

import com.devandre.mediumclone.persistence.entity.User;
import com.devandre.mediumclone.persistence.repository.UserRepository;
import com.devandre.mediumclone.security.jwt.TokenPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.Date;

/**
 * @author Andre on 19/02/2024
 * @project medium-clone
 */
@Component
@RequiredArgsConstructor
public class UserSessionProvider {

    private final transient UserRepository userRepository;

    public Mono<User> getCurrentUserOrEmpty() {
        return getCurrentUserSessionOrEmpty().map(UserSession::user);
    }

    /**
     * This method retrieves the current user session if it exists, or returns an empty Mono otherwise.
     *
     * It first retrieves the security context from the ReactiveSecurityContextHolder. If the context exists,
     * it retrieves the Authentication object from the security context.
     *
     * If the Authentication object is null, it means there is no authenticated user, so it returns an empty Mono.
     *
     * If the Authentication object is not null, it retrieves the principal from the Authentication object,
     * which is a TokenPrincipal object in this case.
     *
     * It then uses the UserRepository to find the user by the ID contained in the TokenPrincipal.
     * If the user is found, it creates a new UserSession object with the user and the token from the TokenPrincipal,
     * and returns a Mono containing this UserSession.
     *
     * @return a Mono<UserSession> representing the current user session, or an empty Mono if there is no authenticated user.
     */
    public Mono<UserSession> getCurrentUserSessionOrEmpty() {
        return ReactiveSecurityContextHolder.getContext()
                .flatMap(securityContext -> {
                    final Authentication authentication = securityContext.getAuthentication();

                    if (authentication == null) {
                        return Mono.empty();
                    }

                    final var tokenPrincipal = (TokenPrincipal) authentication.getPrincipal();

                    return userRepository
                            .findById(tokenPrincipal.userId())
                            .map(user -> new UserSession(user, tokenPrincipal.token(), tokenPrincipal.expirationDate()));
                });
    }

    public record UserSession(User user, String token, Date expirationDate) {
    }

}
