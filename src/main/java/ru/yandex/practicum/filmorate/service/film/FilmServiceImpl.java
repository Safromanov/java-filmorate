package ru.yandex.practicum.filmorate.service.film;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.util.Collection;
import java.util.List;

@Service
@AllArgsConstructor
public class FilmServiceImpl implements FilmService {

    private FilmStorage filmStorage;
    public void likeFilm(Film film, User user){
        user.getLikedFilms().add(film.getId());
        film.getIdUsersWhoLike().add(user.getId());
    }

    public void dislikeFilm(Film film, User user){
        user.getLikedFilms().remove(film.getId());
        film.getIdUsersWhoLike().remove(user.getId());
    }

    @Override
    public Collection<Film> findAll() {
        return filmStorage.findAll();
    }

    @Override
    public Film create(Film film) {
        return filmStorage.create(film);
    }

    @Override
    public Film update(Film film) {
        return filmStorage.update(film);
    }

    @Override
    public Film getFilm(long id) {
        return filmStorage.getFilm(id);
    }

    @Override
    public List<Film> getPopularFilm(int size) {
        return filmStorage.getPopularFilm(size);
    }
}