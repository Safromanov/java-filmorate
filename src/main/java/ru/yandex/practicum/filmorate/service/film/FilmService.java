package ru.yandex.practicum.filmorate.service.film;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.List;

public interface FilmService {
    Collection<Film> findAll();
    Film create(Film film);
    Film update(Film film);
    Film getFilm(long id);
    List<Film> getPopularFilm(int size);
    void likeFilm(Film film, User user);
    void dislikeFilm(Film film, User user);
}
