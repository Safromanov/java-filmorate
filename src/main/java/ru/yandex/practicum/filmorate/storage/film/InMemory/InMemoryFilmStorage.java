package ru.yandex.practicum.filmorate.storage.film.InMemory;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Slf4j
@Repository
@AllArgsConstructor
public class InMemoryFilmStorage implements FilmStorage {

    public List<Film> findAll() {
        return null;
    }

    public Film getFilm(long id) {
        return null;
    }

    @Override
    public Set<Film> getPopularFilm(int size, Integer genreId, Integer year) {
        return null;
    }

    public Film add(Film film) {
        return null;
    }

    public Film update(Film film) {
        return null;
    }


    @Override
    public void addLike(long userId, long filmId) {
    }

    @Override
    public void removeLike(long userId, long filmId) {
    }

    @Override
    public List<Film> createListFilmsById(Collection<Long> filmRecommendations) {
        return null;
    }

    @Override
    public void deleteFilm(long filmId) {
    }

    @Override
    public List<Film> getSortFilmsByDirector(long id, String sortBy) {
        return null;
    }

    @Override
    public List<Film> searchFilms(Map<String, String> searchMap) {
        return null;
    }

    @Override
    public List<Film> getCommonFilms(Integer userId, Integer friendId) {
        return null;
    }
}
