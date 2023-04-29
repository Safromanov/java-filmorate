package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.BaseStorage;

import java.util.Collection;
import java.util.Set;

public interface FilmStorage extends BaseStorage<Film> {

    Film getFilm(long id);

    Set<Film> getPopularFilm(int size,Integer genreId,Integer year);

    void addLike(long userId, long filmId);

    void removeLike(long userId, long filmId);

    void deleteFilm(long filmid);

    Collection<Film> getSortFilmsByDirector(long id, String sortBy);

}
