package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.BaseStorage;

import java.util.Collection;
import java.util.Set;

public interface FilmStorage extends BaseStorage<Film> {

    Film getFilm(long id);

    Set<Film> getPopularFilm(int size);

    void addLike(long userId, long filmId);

    void removeLike(long userId, long filmId);

    Collection<Film> createCollectionFilmsById(Collection<Long> filmRecommendations);
}
