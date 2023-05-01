package ru.yandex.practicum.filmorate.storage.film.directorDb;

import ru.yandex.practicum.filmorate.model.Director;

import java.util.List;
import java.util.Set;

public interface DirectorDb {
    Director createDirector(Director director);

    Director update(Director director);

    Director getDirector(long id);

    Set<Director> findDirectorFilm(long filmId);

    List<Director> findAll();

    Set<Director> addDirectorToFilm(long filmId, Set<Director> genres);

    Set<Director> updateDirectorFilm(long filmId, Set<Director> genres);

    Void deleteDirector(long id);
}