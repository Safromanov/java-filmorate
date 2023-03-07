package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.List;

public interface FilmStorage {
    Collection<Film> findAll();
    Film getFilm(long id);
    Film create(Film film);

    Film update(Film film);
    List<Film> getPopularFilm(int size);

}
