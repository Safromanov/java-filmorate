package ru.yandex.practicum.filmorate.storage.film.mappers;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class FilmExtractor implements ResultSetExtractor<Set<Film>> {

    private final RowMapper<Film> filmMapper;
    private final RowMapper<Genre> genreMapper;
    private final RowMapper<Director> directorMapper;

    @Override
    public Set<Film> extractData(ResultSet rs) throws SQLException, DataAccessException {
        Set<Film> data = new LinkedHashSet<>();
        Map<Long, Film> mapId = new HashMap<>();
        Film film;
        while (rs.next()) {
            Genre genre = genreMapper.mapRow(rs, 1);
            Director director = directorMapper.mapRow(rs, 1);
            long filmId = rs.getLong("film_id");
            if (mapId.containsKey(filmId))
                film = mapId.get(filmId);
            else {
                film = filmMapper.mapRow(rs, 1);
                data.add(film);
                mapId.put(film.getId(), film);
            }
            if (genre != null) {
                film.addGenre(genre);
            }
            if (director != null) {
                film.addDirector(director);
            }
        }
        return data;
    }

}