package com.devandre.mediumclone.dto;

import com.devandre.mediumclone.dto.response.ProfileResponse;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;

/**
 * @author Andre on 19/02/2024
 * @project medium-clone
 */
@Data
@Accessors(chain = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
public class ProfileWrapper {
    @JsonProperty("profile")
    private ProfileResponse content;
}
