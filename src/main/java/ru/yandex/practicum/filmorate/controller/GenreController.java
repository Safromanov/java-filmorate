package ru.yandex.practicum.filmorate.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.film.GenreDb.GenreDBImpl;

import java.util.Collection;


@Slf4j
@RestController
@RequestMapping("/genres")
@AllArgsConstructor
public class GenreController {

    GenreDBImpl genreDAOImpl;

    @GetMapping("{id}")
    public Genre getGenre(@PathVariable int id) {
        return genreDAOImpl.getGenre(id);
    }

    @GetMapping
    public Collection<Genre> getAllGenres() {
        return genreDAOImpl.findAll();
    }
}
