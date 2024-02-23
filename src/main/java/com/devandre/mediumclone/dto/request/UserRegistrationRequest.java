package com.devandre.mediumclone.dto.request;

import com.devandre.mediumclone.persistence.entity.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author Andre on 22/02/2024
 * @project medium-clone
 */
@Data
@Accessors
@Builder
public class UserRegistrationRequest {

    @NotBlank
    private String username;

    @Email
    @NotBlank
    private String email;

    @NotBlank
    private String password;

    public User toUser(final String encodedPassword, final String id) {
        return User.builder()
                .id(id)
                .encodedPassword(encodedPassword)
                .email(email)
                .username(username)
                .build();
    }

    public User toRawUser() {
        return User.builder()
                .email(email)
                .username(username)
                .build();
    }
}
