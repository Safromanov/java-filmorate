package ru.yandex.practicum.filmorate.service.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.List;

public interface FilmService {
    Collection<Film> findAll();
    Film create(Film film);
    Film update(Film film);
    Film getFilm(long id);
    List<Film> getPopularFilm(int size);
    void likeFilm(long filmId, long userId);
    void removeLike(long filmId, long userId);
}
