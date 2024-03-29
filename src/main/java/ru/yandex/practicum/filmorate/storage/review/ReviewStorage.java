package ru.yandex.practicum.filmorate.storage.review;

import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.storage.BaseStorage;

import java.util.Collection;

public interface ReviewStorage extends BaseStorage<Review> {
    Review getReview(long reviewId);

    void deleteReview(long id);

    Collection<Review> findFilmReviews(long filmId, int count);

    void setLike(long reviewId, long userId);

    void removeLike(long reviewId, long userId);

    void setDislike(long reviewId, long userId);

    void removeDislike(long reviewId, long userId);
}