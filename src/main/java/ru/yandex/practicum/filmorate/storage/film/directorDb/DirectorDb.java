package ru.yandex.practicum.filmorate.storage.film.directorDb;

import ru.yandex.practicum.filmorate.model.Director;

import java.util.List;

public interface DirectorDb {
    Director createDirector(Director director);

    Director update(Director director);

    Director getDirector(long id);

    List<Director> findDirectorFilm(long filmId);

    List<Director> findAll();

    List<Director> addDirectorToFilm(long filmId, List<Director> genres);

    List<Director> updateDirectorFilm(long filmId, List<Director> genres);

    Void deleteDirector(long id);

}
