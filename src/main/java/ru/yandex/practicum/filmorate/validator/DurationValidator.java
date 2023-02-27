package ru.yandex.practicum.filmorate.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.Duration;


class DurationValidator implements ConstraintValidator<FilmDuration, Duration> {

    @Override
    public boolean isValid(Duration duration, ConstraintValidatorContext context) {
        return !duration.isNegative();

    }
}