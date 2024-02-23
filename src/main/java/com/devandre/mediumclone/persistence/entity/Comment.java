package com.devandre.mediumclone.persistence.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.Instant;

import static java.util.Optional.ofNullable;

@ToString
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Comment {

    @Id
    @EqualsAndHashCode.Include
    private final String id;

    private String body;

    private String authorId;

    private final Instant createdAt;

    @LastModifiedDate
    private final Instant updatedAt;

    @Builder
    public Comment(final String id,
                   final String body,
                   final String authorId,
                   final Instant createdAt,
                   final Instant updatedAt
    ) {
        this.id = id;
        this.body = body;
        this.authorId = authorId;
        this.createdAt = ofNullable(createdAt).orElse(Instant.now());
        this.updatedAt = ofNullable(updatedAt).orElse(this.createdAt);
    }

    public boolean isAuthor(final User user) {
        return authorId.equals(user.getId());
    }

}
