package ru.yandex.practicum.filmorate.storage.film.GenreDAO;

import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;

public interface GenreDB {

    Genre getGenre(int id);

    List<Genre> findGenresFilm(long filmId);

    List<Genre> findAll();

    List<Genre> addGenresToFilm(long filmId, List<Genre> genres);

    List<Genre> updateGenresFilm(long filmId, List<Genre> genres);

}
