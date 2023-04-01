package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.film.FilmServiceImpl;
import ru.yandex.practicum.filmorate.service.user.UserService;

import javax.validation.Valid;
import java.util.Collection;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/films")
@RequiredArgsConstructor
public class FilmController {

    private final FilmServiceImpl filmService;
    private final UserService userService;

    @GetMapping
    public Collection<Film> findAll() {
        return filmService.findAll();
    }

    @GetMapping("{id}")
    public Film get(@PathVariable long id) {
        return filmService.getFilm(id);
    }

    @PostMapping
    public Film create(@Valid @RequestBody Film film) {
        return filmService.create(film);
    }

    @PutMapping
    public Film update(@Valid @RequestBody Film film) {
        return filmService.update(film);
    }


    @PutMapping("{id}/like/{userId}")
    public void likeFilm(@PathVariable long id, @PathVariable long userId) {
        filmService.likeFilm(filmService.getFilm(id), userService.getUser(userId));
    }

    @DeleteMapping("{id}/like/{userId}")
    public void dislikeFilm(@PathVariable long id, @PathVariable long userId) {
        filmService.dislikeFilm(filmService.getFilm(id), userService.getUser(userId));
    }

    @GetMapping("popular")
    public List<Film> getPopularFilms(@RequestParam(value = "count", defaultValue = "10", required = false) Integer count) {
        return filmService.getPopularFilm(count);
    }

}
