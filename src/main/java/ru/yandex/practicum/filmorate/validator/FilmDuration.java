package ru.yandex.practicum.filmorate.validator;

import javax.validation.Constraint;
import javax.validation.Payload;

import java.lang.annotation.*;

@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = DurationValidator.class)
@Documented
public @interface FilmDuration {

    String message() default "{Duration.invalid}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
