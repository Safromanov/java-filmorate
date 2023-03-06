package ru.yandex.practicum.filmorate.validator;

import javax.validation.ConstraintValidatorContext;
import javax.validation.ConstraintValidator;

import java.time.Duration;


class DurationValidator implements ConstraintValidator<FilmDuration, Duration> {

    @Override
    public boolean isValid(Duration duration, ConstraintValidatorContext context) {
        return duration.getSeconds() >= 1;
    }

}