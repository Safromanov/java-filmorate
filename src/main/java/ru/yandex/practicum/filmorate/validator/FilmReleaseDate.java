package ru.yandex.practicum.filmorate.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target({ ElementType.FIELD,  ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = FilmReleaseDateValidator.class)
@Documented
public @interface FilmReleaseDate {
    String message() default "{ReleaseDate.invalid}";

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };



}
