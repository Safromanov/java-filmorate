package ru.yandex.practicum.filmorate.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDate;


class FilmReleaseDateValidator implements ConstraintValidator<FilmReleaseDate, LocalDate> {

    @Override
    public boolean isValid(LocalDate date, ConstraintValidatorContext context) {
        return date.isAfter(LocalDate.of(1895, 12, 28));

    }
}