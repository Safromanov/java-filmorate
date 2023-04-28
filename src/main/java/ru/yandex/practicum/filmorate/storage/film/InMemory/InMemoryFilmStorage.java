package ru.yandex.practicum.filmorate.storage.film.InMemory;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.cfg.NotYetImplementedException;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.GeneratorId;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

@Slf4j
@Repository
@AllArgsConstructor
public class InMemoryFilmStorage implements FilmStorage {

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

    public Film add(Film film) {
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
    public Set<Film> getPopularFilm(int size) {
        throw new NotYetImplementedException();
    }

    @Override
    public void addLike(long userId, long filmId) {
        throw new NotYetImplementedException();
    }

    @Override
    public void removeLike(long userId, long filmId) {
        throw new NotYetImplementedException();
    }

    @Override
    public Collection<Film> createCollectionFilmsById(Collection<Long> filmRecommendations) {
        return null;
    }
}
