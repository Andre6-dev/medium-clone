package com.devandre.mediumclone.service.user;

import com.devandre.mediumclone.dto.request.UserAuthenticationRequest;
import com.devandre.mediumclone.dto.request.UserRegistrationRequest;
import com.devandre.mediumclone.dto.response.ProfileResponse;
import com.devandre.mediumclone.dto.response.UserResponse;
import com.devandre.mediumclone.persistence.entity.User;
import com.devandre.mediumclone.persistence.repository.UserRepository;
import com.devandre.mediumclone.security.provider.UserSessionProvider;
import com.devandre.mediumclone.security.service.SecuredUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Optional;

/**
 * @author Andre on 16/02/2024
 * @project medium-clone
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepository;

    private final UserSessionProvider userSessionProvider;

    private final SecuredUserService securedUserService;

    public Mono<ProfileResponse> getProfileFinal(final String profileUserName) {
        log.info("Getting profile for user: {}", profileUserName);
        return userSessionProvider.getCurrentUserOrEmpty()
                .flatMap(currentUser -> getProfile(profileUserName, Optional.of(currentUser)))
                .switchIfEmpty(getProfile(profileUserName, Optional.empty()));
    }

    public Mono<ProfileResponse> getProfile(final String profileUserName, final Optional<User> viewerUser) {
        return userRepository.findByUsernameOrFail(profileUserName)
                .flatMap(profileUser -> viewerUser
                        .map(viewer -> Mono.just(ProfileResponse.convertToProfileResponseByViewerUser(profileUser, viewer)))
                        .orElseGet(() -> Mono.just(ProfileResponse.toUnfollowedProfileResponse(profileUser))));
    }

    public Mono<UserResponse> login(final Mono<UserAuthenticationRequest> request) {
        return securedUserService.login(request);
    }

    public Mono<UserResponse> signUp(final Mono<UserRegistrationRequest> request) {
        return request.flatMap(securedUserService::signUp);
    }

}
