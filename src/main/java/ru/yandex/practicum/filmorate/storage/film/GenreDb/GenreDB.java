package ru.yandex.practicum.filmorate.storage.film.GenreDb;

import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;
import java.util.Set;

public interface GenreDB {

    Genre getGenre(int id);

    Set<Genre> findGenresFilm(long filmId);

    List<Genre> findAll();

    Set<Genre> addGenresToFilm(long filmId, Set<Genre> genres);

    Set<Genre> updateGenresFilm(long filmId, Set<Genre> genres);

}
