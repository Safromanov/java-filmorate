package ru.yandex.practicum.filmorate.controller;


import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.GeneratorId;

import javax.validation.Valid;
import java.util.*;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {
    private final Map<Integer, Film> films = new HashMap<>();
    private final GeneratorId generatorId;

    public FilmController(GeneratorId generatorId) {
        this.generatorId = generatorId;
    }

    @GetMapping
    public Collection<Film> findAll() {
        log.debug("Текущее количество пользователей: {}", films.size());
        return films.values();
    }

    @PostMapping
    public Film create(@Valid @RequestBody Film film) {
        film.setId(generatorId.getId());
        log.debug("Новый фильм: {}", film);
        films.put(film.getId(), film);
        return film;
    }

    @PutMapping
    public Film update(@Valid @RequestBody Film film) {
        if (!films.containsKey(film.getId())) throw new ValidationException("Обновляемый фильм отсутствует в базе");
        films.put(film.getId(), film);
        log.debug("Фильм обновлён: {}", film);
        return film;
    }

}
