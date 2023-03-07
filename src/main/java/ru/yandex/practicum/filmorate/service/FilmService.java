package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

@Service
public class FilmService {
    public void likeFilm(Film film, User user){
        user.getLikedFilms().add(film.getId());
        film.getIdUsersWhoLike().add(user.getId());
    }

    public void dislikeFilm(Film film, User user){
        user.getLikedFilms().remove(film.getId());
        film.getIdUsersWhoLike().remove(user.getId());
    }
}
