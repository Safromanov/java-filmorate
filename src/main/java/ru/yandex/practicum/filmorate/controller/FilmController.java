package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import javax.validation.Valid;
import java.util.Collection;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/films")
@RequiredArgsConstructor
public class FilmController {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;
    private final FilmService filmService;


    @GetMapping
    public Collection<Film> findAll() {
        return filmStorage.findAll();
    }
    @GetMapping("{id}")
    public Film get(@PathVariable long id) {
        return filmStorage.getFilm(id);
    }
    @PostMapping
    public Film create(@Valid @RequestBody Film film) {
        return filmStorage.create(film);
    }

    @PutMapping
    public Film update(@Valid @RequestBody Film film) {
        return filmStorage.update(film);
    }


    @PutMapping("{id}/like/{userId}")
    public void likeFilm(@PathVariable long id, @PathVariable long userId){
        filmService.likeFilm(filmStorage.getFilm(id), userStorage.getUser(userId));
    }

    @DeleteMapping("{id}/like/{userId}")
    public void dislikeFilm(@PathVariable long id, @PathVariable long userId){
        filmService.dislikeFilm(filmStorage.getFilm(id), userStorage.getUser(userId));
    }

    @GetMapping("popular")
    public List<Film> getPopularFilms(@RequestParam(value = "count", defaultValue = "10", required = false) Integer count){
        return filmStorage.getPopularFilm(count);
    }

}
