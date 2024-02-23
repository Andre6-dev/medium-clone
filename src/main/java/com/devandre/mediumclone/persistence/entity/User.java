package com.devandre.mediumclone.persistence.entity;

import lombok.*;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.lang.Nullable;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static java.util.Optional.ofNullable;

@Document(collection = "users")
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class User {

    @EqualsAndHashCode.Include
    @Setter(AccessLevel.NONE)
    private String id;

    @Singular
    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    private final List<String> followingIds;

    private String username;

    private String encodedPassword;

    private String email;

    @Nullable
    private String bio;

    @Nullable
    private String image;

    private Instant createdAt;

    @LastModifiedDate
    private Instant updatedAt;

    @Builder(toBuilder = true)
    public User(final String id,
                @Nullable final List<String> followingIds,
                final String username,
                final String encodedPassword,
                final String email,
                @Nullable final String bio,
                @Nullable final String image
    ) {
        this.id = id;
        this.followingIds = ofNullable(followingIds).orElse(new ArrayList<>());
        this.username = username;
        this.encodedPassword = encodedPassword;
        this.email = email;
        this.bio = bio;
        this.image = image;
    }

    public List<String> getFollowingIds() {
        // Return an unmodifiable list to prevent modification
        return Collections.unmodifiableList(followingIds);
    }

    public void follow(final String userId) {
        followingIds.add(userId);
    }

    public void unfollow(final String userId) {
        followingIds.remove(userId);
    }

    public void follow(final User user) {
        follow(user.getId());
    }

    public void unfollow(final User user) {
        unfollow(user.getId());
    }

    public boolean isFollowing(final User user) {
        return this.followingIds.contains(user.getId());
    }

    public boolean isFollowedBy(final User user) {
        return user.isFollowing(this);
    }
}
