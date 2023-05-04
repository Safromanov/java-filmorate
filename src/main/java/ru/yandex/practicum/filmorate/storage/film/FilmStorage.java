package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.BaseStorage;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface FilmStorage extends BaseStorage<Film> {

    Film getFilm(long id);

    Set<Film> getPopularFilm(int size, Integer genreId, Integer year);

    void addLike(long userId, long filmId);

    void removeLike(long userId, long filmId);

    Collection<Film> createCollectionFilmsById(Collection<Long> filmRecommendations);

    void deleteFilm(long filmId);

    List<Film> getSortFilmsByDirector(long id, String sortBy);


    Collection<Film> searchFilms(Map<String, String> searchMap);

    List<Film> getCommonFilms(Integer userId, Integer friendId);
}