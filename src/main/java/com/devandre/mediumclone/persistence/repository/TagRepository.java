package com.devandre.mediumclone.persistence.repository;

import com.devandre.mediumclone.persistence.entity.Tag;
import com.mongodb.DuplicateKeyException;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;

import java.util.function.BiConsumer;

public interface TagRepository extends ReactiveMongoRepository<Tag, String> {

    default Flux<Tag> saveAllTags(final Iterable<String> tags) {
        return Flux.fromIterable(tags)
                .flatMap(it -> save(Tag.makeInstance(it)))
                .onErrorContinue(DuplicateKeyException.class, nothing());
    }

    /**
     * This private method returns a BiConsumer that does nothing.
     * It is used as the error handler in the onErrorContinue() operator in the saveAllTags() method.
     *
     * @return A BiConsumer that does nothing.
     */
    private BiConsumer<Throwable, Object> nothing() {
        return (throwable, o) -> {
        };
    }

}
