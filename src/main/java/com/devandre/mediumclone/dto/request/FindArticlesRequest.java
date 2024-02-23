package com.devandre.mediumclone.dto.request;

import com.devandre.mediumclone.persistence.entity.User;
import lombok.*;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;

/**
 * @author Andre on 15/02/2024
 * @project medium-clone
 */
@Data
@Accessors(chain = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FindArticlesRequest {

    private int limit = 0;
    private int offset = 20;
    private String authorId = null;
    private String tag = null;

    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    private User favoritedBy = null;

    public User getFavoritedBy() {
        return this.favoritedBy.toBuilder().build();
    }

    public FindArticlesRequest setFavoritedBy(User favoritedBy) {
        this.favoritedBy = favoritedBy.toBuilder().build();
        return this;
    }
}
