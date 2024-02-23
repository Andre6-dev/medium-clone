package com.devandre.mediumclone.dto.request;

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
@Accessors(chain = true)
@Builder
public class UserAuthenticationRequest {

    @Email
    @NotBlank
    private String email;

    @NotBlank
    private String password;
}
