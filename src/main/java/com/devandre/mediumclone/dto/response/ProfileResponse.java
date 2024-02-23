package com.devandre.mediumclone.dto.response;

import com.devandre.mediumclone.persistence.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @author Andre on 19/02/2024
 * @project medium-clone
 */
@Data
@Accessors(chain = true)
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProfileResponse {

    private String username;

    private String bio;

    private String image;

    private boolean following;

    public static ProfileResponse toUnfollowedProfileResponse(final User profileUser) {
        return toProfileResponse(profileUser, false);
    }

    public static ProfileResponse toFollowedProfileResponse(final User profileUser) {
        return toProfileResponse(profileUser, true);
    }

    public static ProfileResponse toOwnProfile(final User user) {
        return convertToProfileResponseByViewerUser(user, user);
    }

    public static ProfileResponse convertToProfileResponseByViewerUser(final User profileUser, final User viewerUser) {
        return toProfileResponse(profileUser, profileUser.isFollowedBy(viewerUser));
    }

    /**
     * Convert User to ProfileResponse
     * @param profileUser: User
     * @param beingFollowed: boolean
     * @return ProfileResponse
     */
    private static ProfileResponse toProfileResponse(final User profileUser, final boolean beingFollowed) {
        return ProfileResponse.builder()
                .username(profileUser.getUsername())
                .bio(profileUser.getBio())
                .image(profileUser.getImage())
                .following(beingFollowed).build();
    }
}
