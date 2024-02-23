package com.devandre.mediumclone.persistence.entity;

import lombok.*;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import static java.util.Optional.ofNullable;

@Document(collection = "articles")
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Article {

    public static final String CREATED_AT_FIELD_NAME = "createdAt";
    public static final String FAVORITING_USER_IDS = "favoritingUserIds";
    public static final String AUTHOR_ID_FIELD_NAME = "authorId";
    public static final String TAGS_FIELD_NAME = "tags";

    @Getter
    @EqualsAndHashCode.Include
    private final String id;

    @Getter
    private final Instant createdAt;

    @Getter
    @LastModifiedDate
    private final Instant updatedAt;

    @Getter
    private final List<String> tags;

    @Getter
    private final List<Comment> comments;

    @Getter
    private final List<String> favouringUserIds;

    @Getter
    private String slug;

    @Getter
    private String title;

    @Getter
    @Setter
    private String description;

    @Getter
    @Setter
    private String body;

    @Getter
    @Setter
    private String authorId;

    @Builder
    public Article(final String id,
                   final Instant createdAt,
                   final Instant updatedAt,
                   final List<String> tags,
                   final List<Comment> comments,
                   final List<String> favouringUserIds,
                   final String slug,
                   final String title,
                   final String description,
                   final String body,
                   final String authorId
    ) {
        this.id = id;
        this.title = title;
        this.slug = toSlug(title);
        this.description = description;
        this.body = body;
        this.createdAt = ofNullable(createdAt).orElse(Instant.now());
        this.updatedAt = ofNullable(updatedAt).orElse(createdAt);
        this.authorId = authorId;
        this.tags = ofNullable(tags).orElse(new ArrayList<>());
        this.comments = ofNullable(comments).orElse(new ArrayList<>());
        this.favouringUserIds = ofNullable(favouringUserIds).orElse(new ArrayList<>());
    }

    public int getFavoritesCount() {
        return favouringUserIds == null ? 0 : favouringUserIds.size();
    }

    public Article addComment(final Comment comment) {
        comments.add(comment);
        return this;
    }

    public Article removeComment(final Comment comment) {
        comments.remove(comment);
        return this;
    }

    public void setTitle(final String title) {
        this.title = title;
        this.slug = toSlug(title);
    }

    public Optional<Comment> getCommentById(final String commentId) {
        return comments.stream()
                .filter(comment -> comment.getId()
                        .equals(commentId))
                .findFirst();
    }

    public boolean hasTag(final String tag) {
        return tags.contains(tag);
    }

    public boolean isAuthor(final String authorId) {
        return this.authorId.equals(authorId);
    }

    public boolean isAuthor(final User author) {
        return isAuthor(author.getId());
    }

    public boolean favoriteByUser(final User user) {
        if (favouringUserIds.contains(user.getId())) {
            return false;
        }
        favouringUserIds.add(user.getId());
        return true;
    }

    public boolean unfavoriteByUser(final User user) {
        if (!favouringUserIds.contains(user.getId())) {
            return false;
        }
        favouringUserIds.remove(user.getId());
        return true;
    }

    private static String toSlug(final String title) {
        return title.toLowerCase(Locale.US)
                .replaceAll(
                        "[&|\\uFE30-\\uFFA0’”\\s?,.]+",
                        "-"
                );
    }
}
