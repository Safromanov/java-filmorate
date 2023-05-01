package ru.yandex.practicum.filmorate.service.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.Map;
import java.util.List;
import java.util.Set;

@SuppressWarnings("checkstyle:Regexp")
public interface FilmService {

    Collection<Film> findAll();

    Film create(Film film);

    Film update(Film film);

    Film getFilm(long id);

    Set<Film> getPopularFilm(int size,Integer genreId,Integer year);

    void likeFilm(long filmId, long userId);

    void removeLike(long filmId, long userId);

    void deleteFilm(long filmId);

    Collection<Film> getSortFilmsByDirector(long id, String count);

    Collection<Film> searchFilms(Map<String, String> searchMap);
    List<Film> getCommonFilms(Integer userId, Integer friendId);

}
