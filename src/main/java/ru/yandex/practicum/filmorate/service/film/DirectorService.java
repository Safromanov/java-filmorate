package ru.yandex.practicum.filmorate.service.film;

import ru.yandex.practicum.filmorate.model.Director;

import java.util.List;

public interface DirectorService {
    Director getDirector(long id);

    Director createDirector(Director director);

    List<Director> findAllDirectors();

    Director update(Director director);

    Void deleteDirector(long id);
}