package com.devandre.mediumclone.persistence.repository;

import com.devandre.mediumclone.dto.request.FindArticlesRequest;
import com.devandre.mediumclone.persistence.entity.Article;
import com.devandre.mediumclone.persistence.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.lang.Nullable;
import reactor.core.publisher.Flux;

import static java.util.Optional.ofNullable;
import static org.springframework.data.mongodb.core.query.Criteria.where;

public interface ArticleManualRepository {

    Flux<Article> findNewestArticlesFilteredBy(@Nullable String tag,
                                               @Nullable String authorId,
                                               @Nullable User favoritingUser,
                                               int limit,
                                               int offset);

    default Flux<Article> findNewestArticlesFilteredBy(final FindArticlesRequest request) {
        return findNewestArticlesFilteredBy(
                request.getTag(),
                request.getAuthorId(),
                request.getFavoritedBy(),
                request.getLimit(),
                request.getOffset()
        );
    }
}

/**
 * This class implements the ArticleManualRepository interface.
 * It uses a ReactiveMongoTemplate to execute queries against the MongoDB database.
 */
@RequiredArgsConstructor
@Slf4j
class ArticleManualRepositoryImpl implements ArticleManualRepository {

    private final ReactiveMongoTemplate mongoTemplate;

    /**
     * This method retrieves the newest articles filtered by the provided parameters.
     * It builds a MongoDB query based on the parameters, and executes the query
     * using the ReactiveMongoTemplate.
     * It returns a Flux of Articles.
     *
     * @param tag The tag to filter articles by. Can be null.
     * @param authorId The author ID to filter articles by. Can be null.
     * @param favoritingUser The user to filter articles by. Can be null.
     * @param limit The maximum number of articles to return.
     * @param offset The number of articles to skip before starting to return articles.
     * @return A Flux of filtered Articles.
     */
    @Override
    public Flux<Article> findNewestArticlesFilteredBy(String tag,
                                                      String authorId,
                                                      User favoritingUser,
                                                      int limit,
                                                      int offset) {
        final Query query = new Query().skip(offset)
                .limit(limit)
                .with(ArticleRepository.NEWEST_ARTICLE_SORT);

        ofNullable(favoritingUser)
                .ifPresent(user -> query.addCriteria(favoritedByUser(user)));

        ofNullable(tag)
                .ifPresent(it -> query.addCriteria(tagsContains(it)));

        ofNullable(authorId)
                .ifPresent(it -> query.addCriteria(authorIdEquals(it)));

        return mongoTemplate.find(query, Article.class);
    }

    /**
     * This method returns a Criteria object that checks if the author ID of an article equals the provided ID.
     *
     * @param it The author ID to check.
     * @return A Criteria object.
     */
    private static Criteria authorIdEquals(final String it) {
        return where(Article.AUTHOR_ID_FIELD_NAME).is(it);
    }

    /**
     * This method returns a Criteria object that checks if the tags of an article contain the provided tag.
     *
     * @param it The tag to check.
     * @return A Criteria object.
     */
    private static Criteria tagsContains(final String it) {
        return where(Article.TAGS_FIELD_NAME).is(it);
    }

    /**
     * This method returns a Criteria object that checks if the favoriting user IDs of an article contain the ID of the provided user.
     *
     * @param user The user to check.
     * @return A Criteria object.
     */
    private static Criteria favoritedByUser(final User user) {
        return where(Article.FAVORITING_USER_IDS).in(user.getId());
    }
}

