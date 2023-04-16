package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.BaseStorage;

import java.util.List;

public interface FilmStorage extends BaseStorage<Film> {

    Film getFilm(long id);

    List<Film> getPopularFilm(int size);

    void addLike(long userId, long filmId);

    void removeLike(long userId, long filmId);

}
