package com.devandre.mediumclone.controller;

import com.devandre.mediumclone.dto.ProfileWrapper;
import com.devandre.mediumclone.dto.request.UserAuthenticationRequest;
import com.devandre.mediumclone.dto.request.UserRegistrationRequest;
import com.devandre.mediumclone.dto.response.ProfileResponse;
import com.devandre.mediumclone.security.provider.UserSessionProvider;
import com.devandre.mediumclone.service.user.UserService;
import com.devandre.mediumclone.utils.Constants;
import com.devandre.mediumclone.utils.ResponseHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@RequestMapping(Constants.API_BASE_PATH + "users")
public class UserController {

    private final UserService userService;

    private final UserSessionProvider userSessionProvider;

    @GetMapping("/{username}")
    public Mono<ResponseEntity<Object>> getProfile(@PathVariable("username") final String username) {
        return ResponseHandler.generateMonoResponse(
                HttpStatus.OK,
                userService.getProfileFinal(username)
                        .map(Object.class::cast),
                true
        );
    }

    @PostMapping("/login")
    public Mono<ResponseEntity<Object>> login(@RequestBody final Mono<UserAuthenticationRequest> request) {
        return ResponseHandler.generateMonoResponse(
                HttpStatus.OK,
                userService.login(request)
                        .map(Object.class::cast),
                true
        );
    }

    @PostMapping("/signup")
    public Mono<ResponseEntity<Object>> signUp(@RequestBody final Mono<UserRegistrationRequest> request) {
        return ResponseHandler.generateMonoResponse(
                HttpStatus.CREATED,
                userService.signUp(request)
                        .map(Object.class::cast),
                true
        );
    }

}
