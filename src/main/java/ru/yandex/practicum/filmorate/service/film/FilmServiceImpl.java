package ru.yandex.practicum.filmorate.service.film;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.EventType;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.OperationType;
import ru.yandex.practicum.filmorate.storage.event.EventStorage;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.film.GenreDb.GenreDB;
import ru.yandex.practicum.filmorate.storage.film.directorDb.DirectorDb;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
@AllArgsConstructor
public class FilmServiceImpl implements FilmService {

    private final FilmStorage filmStorage;

    private final GenreDB genreDB;

    private final DirectorDb directorDb;

    private final EventStorage eventStorage;


    public void likeFilm(long filmId, long userId) {
        filmStorage.addLike(filmId, userId);
        eventStorage.addToEventFeed(userId, filmId, EventType.LIKE, OperationType.ADD);
    }

    public void removeLike(long filmId, long userId) {
        filmStorage.removeLike(userId, filmId);
        eventStorage.addToEventFeed(userId, filmId, EventType.LIKE, OperationType.REMOVE);
    }

//    @Override
//    public void deleteFilm(long filmId) {
//        filmStorage.deleteFilm(filmId);
//    }

    @Override
    public Collection<Film> getSortFilmsByDirector(long id, String sortBy) {
        return filmStorage.getSortFilmsByDirector(id, sortBy);
    }

    @Override
    public Collection<Film> findAll() {
        return filmStorage.findAll();
    }

    @Override
    public Film create(Film film) {
        filmStorage.add(film);
        film.setGenres(genreDB.addGenresToFilm(film.getId(), film.getGenres()));
        film.setDirectors(directorDb.addDirectorToFilm(film.getId(), film.getDirectors()));
        return film;
    }

    @Override
    public Film update(Film film) {
        filmStorage.update(film);
        film.setGenres(genreDB.updateGenresFilm(film.getId(), film.getGenres()));
        film.setDirectors(directorDb.updateDirectorFilm(film.getId(), film.getDirectors()));
        return film;
    }

    @Override
    public Film getFilm(long id) {
        return filmStorage.getFilm(id);
    }

    @Override
    public Set<Film> getPopularFilm(int size) {
        return null;
    }

    @Override
    public Set<Film> getPopularFilm(int size, Integer genreId, Integer year) {
        return filmStorage.getPopularFilm(size, genreId, year);
    }

    @Override
    public Collection<Film> searchFilms(Map<String, String> searchMap) {
        return filmStorage.searchFilms(searchMap);
    }


    public List<Film> getCommonFilms(Integer userId, Integer friendId) {
        return filmStorage.getCommonFilms(userId,friendId);
    }
}
