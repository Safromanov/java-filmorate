package ru.yandex.practicum.filmorate.service.film;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.film.GenreDB;

import java.util.Collection;
import java.util.Set;

@Service
@AllArgsConstructor
public class FilmServiceImpl implements FilmService {


    private final FilmStorage filmStorage;

    private final GenreDB genreDB;


    public void likeFilm(long filmId, long userId) {
        filmStorage.addLike(filmId, userId);
    }

    public void removeLike(long filmId, long userId) {
        filmStorage.removeLike(userId, filmId);
    }

    @Override
    public Collection<Film> findAll() {
        return filmStorage.findAll();
    }

    @Override
    public Film create(Film film) {
        filmStorage.addFilm(film);

        film.setGenres(genreDB.addGenresToFilm(film.getId(), film.getGenres()));

        return film;
    }

    @Override
    public Film update(Film film) {
        filmStorage.update(film);

        film.setGenres(genreDB.updateGenresFilm(film.getId(), film.getGenres()));

        return film;
    }

    @Override
    public Film getFilm(long id) {
        return filmStorage.getFilm(id);
    }

    @Override
    public Set<Film> getPopularFilm(int size) {
        return filmStorage.getPopularFilm(size);
    }

}
