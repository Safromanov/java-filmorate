package ru.yandex.practicum.filmorate.validator;

import javax.validation.ConstraintValidatorContext;
import javax.validation.ConstraintValidator;

import java.time.Duration;


class DurationValidator implements ConstraintValidator<FilmDuration, Duration> {

    @Override
    public boolean isValid(Duration duration, ConstraintValidatorContext context) {
         return duration != null && duration.toSeconds() >= 1;
        /*В 2010 году в Нидерландах режиссер по имени Антон Корбейн
        снял короткометражную кинокартину под названием «Самый короткий фильм».
        Продолжительность этого фильма составляет всего одну секунду.
        Сюжет фильма достаточно прост: актриса стоит на фоне мельницы, тут появляется рука режиссера Антона Корбейна,
        и героиня кусает его за палец. Благодаря своей продолжительности эта работа вошла в Книгу рекордов Гиннесса
        как самый короткий фильм в мире. */
    }
}