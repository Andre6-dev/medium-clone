package com.devandre.mediumclone.persistence.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "tags")
@Builder
@Data
@AllArgsConstructor
public class Tag {

    @Id
    private String id;

    @Indexed(unique = true)
    private final String tagName;

    /**
     * Make a new instance of Tag
     * @param tag the tag name
     * @return the makeInstance() method is a convenient way to create new Tag objects without specifying an id.
     */
    public static Tag makeInstance(final String tag) {
        return new Tag(null, tag);
    }
}
