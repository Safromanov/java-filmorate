package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.film.FilmService;

import javax.validation.Valid;
import java.util.*;


@SuppressWarnings("checkstyle:Regexp")
@Slf4j
@RestController
@RequestMapping("/films")
@RequiredArgsConstructor
public class FilmController {

    private final FilmService filmService;

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
        filmService.likeFilm(id, userId);
    }

    @DeleteMapping("{id}/like/{userId}")
    public void dislikeFilm(@PathVariable long id, @PathVariable long userId) {
        filmService.removeLike(id, userId);
    }

//    @DeleteMapping("{filmId}")
//    public void deleteFilm(@PathVariable long filmId) {
//        filmService.deleteFilm(filmId);
//    }

    @GetMapping("popular")
    public Set<Film> getPopularFilms(
            @RequestParam(value = "count", defaultValue = "10", required = false) Integer count,
            @RequestParam(value = "genreId", defaultValue = "-1", required = false) Integer genreId,
            @RequestParam(value = "year", defaultValue = "-1", required = false) Integer year
    ) {
        return filmService.getPopularFilm(count, genreId, year);
    }

    @GetMapping("director/{id}")
    public Collection<Film> getSortFilmsByDirector(@PathVariable long id, @RequestParam String sortBy) {
        return filmService.getSortFilmsByDirector(id, sortBy);
    }


    @GetMapping("/search")
    public Collection<Film> searchFilms(
            @RequestParam(value = "query", defaultValue = "", required = false) String query,
            @RequestParam(value = "by", required = false) List<String> by) {
        Map<String, String> searchMap = new HashMap<>();
        if (query == null || query.isBlank()) {
            return filmService.searchFilms(searchMap);
        }
        if (by != null) {
            by.stream()
                    .filter(s -> s != null && !s.isBlank())
                    .map(s -> s.toLowerCase().trim())
                    .filter(s -> "director".equals(s) || "title".equals(s))
                    .distinct()
                    .forEach(s -> searchMap.put(s, query));
        }
        return filmService.searchFilms(searchMap);
    }

//    @GetMapping("common")
//    public List<Film> getCommonFilms(
//            @RequestParam(value = "userId") Integer userId,
//            @RequestParam(value = "friendId") Integer friendId
//            ) {
//        return filmService.getCommonFilms(userId,friendId);
//    }

}
