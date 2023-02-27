package ru.yandex.practicum.filmorate.validator;

import javax.validation.ConstraintValidatorContext;
import javax.validation.ConstraintValidator;

import java.time.LocalDate;


class FilmReleaseDateValidator implements ConstraintValidator<FilmReleaseDate, LocalDate> {

    @Override
    public boolean isValid(LocalDate date, ConstraintValidatorContext context) {
        return date.isAfter(LocalDate.of(1895, 12, 28));

    }
}