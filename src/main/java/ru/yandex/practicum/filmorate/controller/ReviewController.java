package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.service.review.ReviewService;

import javax.validation.Valid;
import java.util.Collection;

@Slf4j
@RestController
@RequestMapping("/reviews")
@RequiredArgsConstructor
public class ReviewController {
    private final ReviewService reviewService;

    @GetMapping
    public Collection<Review> findAll(@RequestParam(required = false) Long filmId,
                                      @RequestParam(defaultValue = "10", required = false) Integer count) {
        if (filmId != null) {
            return reviewService.findFilmReviews(filmId, count);
        } else {
            return reviewService.findAll();
        }
    }

    @GetMapping("{reviewId}")
    public Review getReview(@PathVariable long reviewId) {
        return reviewService.getReview(reviewId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Review create(@Valid @RequestBody Review review) {
        return reviewService.create(review);
    }

    @PutMapping
    public Review update(@Valid @RequestBody Review review) {
        return reviewService.update(review);
    }

    @DeleteMapping("{reviewId}")
    public void deleteReview(@PathVariable long reviewId) {
        reviewService.deleteReview(reviewId);
    }

    @PutMapping("{reviewId}/like/{userId}")
    public void setLike(@PathVariable long reviewId, @PathVariable long userId) {
        reviewService.setLike(reviewId, userId);
    }

    @DeleteMapping("{reviewId}/like/{userId}")
    public void removeLike(@PathVariable long reviewId, @PathVariable long userId) {
        reviewService.removeLike(reviewId, userId);
    }

    @PutMapping("{reviewId}/dislike/{userId}")
    public void setDislike(@PathVariable long reviewId, @PathVariable long userId) {
        reviewService.setDislike(reviewId, userId);
    }

    @DeleteMapping("{reviewId}/dislike/{userId}")
    public void removeDislike(@PathVariable long reviewId, @PathVariable long userId) {
        reviewService.removeDislike(reviewId, userId);
    }
}