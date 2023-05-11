package ru.yandex.practicum.filmorate.service.review;

import ru.yandex.practicum.filmorate.model.Review;

import java.util.Collection;

public interface ReviewService {
    Collection<Review> findAll();

    Review getReview(long reviewId);

    Review create(Review review);

    Review update(Review review);

    void deleteReview(long reviewId);

    Collection<Review> findFilmReviews(long filmId, int count);

    void setLike(long reviewId, long userId);

    void removeLike(long reviewId, long userId);

    void setDislike(long reviewId, long userId);

    void removeDislike(long reviewId, long userId);
}