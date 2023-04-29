package ru.yandex.practicum.filmorate.service.review;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.EventType;
import ru.yandex.practicum.filmorate.model.OperationType;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.storage.event.EventStorage;
import ru.yandex.practicum.filmorate.storage.review.ReviewStorage;

import java.util.Collection;

@Service
@AllArgsConstructor
public class ReviewService {
    private final ReviewStorage reviewStorage;
    private final EventStorage eventStorage;

    public Collection<Review> findAll() {
        return reviewStorage.findAll();
    }

    public Review getReview(long reviewId) {
        return reviewStorage.getReview(reviewId);
    }

    public Review create(Review review) {
        Review rev = reviewStorage.add(review);
        eventStorage.addToEventFeed(rev.getUserId(), rev.getId(),
                EventType.REVIEW, OperationType.ADD);
        return rev;
    }

    public Review update(Review review) {
        Review rev = reviewStorage.update(review);
        eventStorage.addToEventFeed(rev.getUserId(), rev.getId(),
                EventType.REVIEW, OperationType.UPDATE);
        return rev;
    }

    public void deleteReview(long reviewId) {
        long userId = getReview(reviewId).getUserId();
        reviewStorage.deleteReview(reviewId);
        eventStorage.addToEventFeed(userId, reviewId,
                EventType.REVIEW, OperationType.REMOVE);
    }

    public Collection<Review> findFilmReviews(long filmId, int count) {
        return reviewStorage.findFilmReviews(filmId, count);
    }

    public void setLike(long reviewId, long userId) {
        reviewStorage.setLike(reviewId, userId);
    }

    public void removeLike(long reviewId, long userId) {
        reviewStorage.removeLike(reviewId, userId);
    }

    public void setDislike(long reviewId, long userId) {
        reviewStorage.setDislike(reviewId, userId);
    }

    public void removeDislike(long reviewId, long userId) {
        reviewStorage.removeDislike(reviewId, userId);
    }
}