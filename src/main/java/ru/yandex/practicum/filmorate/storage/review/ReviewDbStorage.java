package ru.yandex.practicum.filmorate.storage.review;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.storage.film.mappers.ReviewMapper;

import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Repository
@Slf4j
public class ReviewDbStorage implements ReviewStorage {

    private final JdbcTemplate jdbcTemplate;
    private final ReviewMapper reviewMapper;

    @Override
    public List<Review> findAll() {
        String sql = "SELECT * FROM reviews";
        return jdbcTemplate.query(sql, reviewMapper)
                .stream()
                .sorted(Comparator.comparing(Review::getUseful).reversed())
                .collect(Collectors.toList());
    }

    @Override
    public Review add(Review review) {
        idValidation("filmId, userId", review.getFilmId(), review.getUserId());
        var simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("REVIEWS")
                .usingGeneratedKeyColumns("REVIEW_ID")
                .usingColumns("CONTENT", "IS_POSITIVE", "USER_ID", "FILM_ID", "USEFUL");
        long reviewId = simpleJdbcInsert.executeAndReturnKey(reviewToMap(review)).longValue();
        log.debug("Добавлен отзыв: " + review);
        return getReview(reviewId);
    }

    @Override
    public Review update(Review review) {
        idValidation("filmId, userId", review.getFilmId(), review.getUserId());
        String sql = "UPDATE REVIEWS SET CONTENT = ?, IS_POSITIVE = ? WHERE REVIEW_ID = ?";
        jdbcTemplate.update(sql, review.getContent(), review.getIsPositive(), review.getId());
        log.debug("Обновлён отзыв: " + review);
        return getReview(review.getId());
    }

    @Override
    public void deleteReview(long reviewId) {
        String sql = "DELETE FROM REVIEWS WHERE REVIEW_ID = ?";
        jdbcTemplate.update(sql, reviewId);
    }

    @Override
    public Collection<Review> findFilmReviews(long filmId, int count) {
        String sql = "SELECT * FROM reviews WHERE FILM_ID = ? LIMIT ?";
        return jdbcTemplate.query(sql, reviewMapper, filmId, count)
                .stream()
                .sorted(Comparator.comparing(Review::getUseful).reversed())
                .collect(Collectors.toList());
    }

    @Override
    public void setLike(long reviewId, long userId) {
        if (getReview(reviewId) == null) {
            throw new ValidationException("Отзыва с id " + reviewId + "не существует");
        }
        idValidation("reviewId, userId", reviewId, userId);
        String sql = "MERGE INTO LIKES_REVIEW KEY (REVIEW_ID, USER_ID) VALUES (?, ?, TRUE)";
        jdbcTemplate.update(sql, reviewId, userId);
        updateReviewLikes(reviewId);
    }

    @Override
    public void removeLike(long reviewId, long userId) {
        idValidation("reviewId, userId", reviewId, userId);
        String sql = "DELETE FROM LIKES_REVIEW WHERE REVIEW_ID = ? AND USER_ID = ?";
        jdbcTemplate.update(sql, reviewId, userId);
        updateReviewLikes(reviewId);
    }

    @Override
    public void setDislike(long reviewId, long userId) {
        if (getReview(reviewId) == null) {
            throw new ValidationException("Отзыва с id " + reviewId + "не существует");
        }
        idValidation("reviewId, userId", reviewId, userId);
        String sql = "MERGE INTO LIKES_REVIEW KEY (REVIEW_ID, USER_ID) VALUES (?, ?, FALSE)";
        jdbcTemplate.update(sql, reviewId, userId);
        updateReviewLikes(reviewId);
    }

    @Override
    public void removeDislike(long reviewId, long userId) {
        idValidation("reviewId, userId", reviewId, userId);
        String sql = "DELETE FROM LIKES_REVIEW WHERE REVIEW_ID = ? AND USER_ID = ?";
        jdbcTemplate.update(sql, reviewId, userId);
        updateReviewLikes(reviewId);
    }

    private void updateReviewLikes(long reviewId) {
        var namedJdbcTemplate = new NamedParameterJdbcTemplate(jdbcTemplate);
        String sql = "UPDATE REVIEWS SET USEFUL = ( " +
                "SELECT SUM(CASE WHEN is_positive THEN 1 ELSE -1 END) " +
                "FROM LIKES_REVIEW " +
                "WHERE REVIEW_ID = :review_id) " +
                "WHERE REVIEW_ID = :review_id";
        namedJdbcTemplate.update(sql, Collections.singletonMap("review_id", reviewId));
    }

    public Review getReview(long reviewId) {
        String sql = "SELECT * FROM REVIEWS WHERE REVIEW_ID = ?";
        List<Review> film = jdbcTemplate.query(sql, reviewMapper, reviewId);
        if (!film.isEmpty()) {
            return film.get(0);
        } else {
            throw new ValidationException("Отзыва с id: " + reviewId + " не существует");
        }
    }

    private Map<String, Object> reviewToMap(Review review) {
        Map<String, Object> values = new HashMap<>();
        values.put("CONTENT", review.getContent());
        values.put("IS_POSITIVE", review.getIsPositive());
        values.put("USER_ID", review.getUserId());
        values.put("FILM_ID", review.getFilmId());
        values.put("USEFUL", review.getUseful());
        return values;
    }

    private void idValidation(String paramName, long... ids) {
        for (long id : ids) {
            if (id <= 0) {
                throw new ValidationException("Передан отрицательный id для " + paramName);
            }
        }
    }
}