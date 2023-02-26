package ru.yandex.practicum.filmorate.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target({ ElementType.FIELD,  ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = DurationValidator.class)
@Documented
public @interface DurationE {
    String message() default "{Duration.invalid}";

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };



}
