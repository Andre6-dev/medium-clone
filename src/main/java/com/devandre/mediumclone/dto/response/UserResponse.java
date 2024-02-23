package com.devandre.mediumclone.dto.response;

import com.devandre.mediumclone.persistence.entity.User;
import com.devandre.mediumclone.security.provider.UserSessionProvider;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author Andre on 22/02/2024
 * @project medium-clone
 */
@Data
@Accessors(chain = true)
@Builder
public class UserResponse {

    private String email;

    private String token;

    private String username;

    private String bio;

    private String image;

    private String expirationTime;

    public static UserResponse fromUserAndToken(final UserSessionProvider.UserSession userSession) {
        return fromUserAndToken(userSession.user(), userSession.token(), userSession.expirationDate());
    }

    public static UserResponse fromUserAndToken(final User user, final String token, final Date expirationTime) {

        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        String formattedDate = formatter.format(expirationTime);

        return UserResponse.builder()
                .username(user.getUsername())
                .email(user.getEmail())
                .bio(user.getBio())
                .image(user.getImage())
                .expirationTime(formattedDate)
                .token(token).build();
    }

}
