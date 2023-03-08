package ru.yandex.practicum.filmorate.storage.film;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.GeneratorId;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Component
@AllArgsConstructor
public class InMemoryFilmStorage implements FilmStorage{
    private final Map<Long, Film> films;
    private final GeneratorId generatorId;


    public Collection<Film> findAll() {
        log.debug("Текущее количество пользователей: {}", films.size());
        return films.values();
    }

    public Film getFilm(long id) {
        Film film = films.get(id);
        if (film == null) throw new ValidationException("Введён не существующий id");
        return film;
    }

    public Film create(Film film) {
        film.setId(generatorId.getId());
        log.debug("Новый фильм: {}", film);
        films.put(film.getId(), film);
        return film;
    }

    public Film update(Film film) {
        var updatedFilm = films.get(film.getId());
        if (updatedFilm == null) throw new ValidationException("Обновляемый фильм отсутствует в базе");
        film.setIdUsersWhoLike(updatedFilm.getIdUsersWhoLike());
        films.put(film.getId(), film);
        log.debug("Фильм обновлён: {}", film);
        return film;
    }

    @Override
    public List<Film> getPopularFilm(int size) {
        return  films.values().stream()
                .sorted((x, y) -> x.getIdUsersWhoLike().size() <= y.getIdUsersWhoLike().size()? 1: -1)
                .limit(size).collect(Collectors.toList());
    }

}
