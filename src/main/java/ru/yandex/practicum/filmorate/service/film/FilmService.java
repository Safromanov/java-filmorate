package ru.yandex.practicum.filmorate.service.film;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

public interface FilmService extends FilmStorage {
    void likeFilm(Film film, User user);
    void dislikeFilm(Film film, User user);
}
