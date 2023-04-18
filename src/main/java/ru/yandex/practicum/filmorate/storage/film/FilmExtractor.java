package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

import static ru.yandex.practicum.filmorate.model.Film.makeFilm;
import static ru.yandex.practicum.filmorate.model.Genre.makeGenre;

@Component
public class FilmExtractor implements ResultSetExtractor<Map<Film, List<Genre>>> {

    @Override
    public Map<Film, List<Genre>> extractData(ResultSet rs) throws SQLException, DataAccessException {

        Map<Film, List<Genre>> data = new LinkedHashMap<>();
        Map<Long, Film> mapId = new HashMap<>();
        while (rs.next()) {
            var genre = makeGenre(rs);
            var filmId = rs.getLong("film_id");
            Film film;
            if (mapId.containsKey(filmId))
                film = mapId.get(filmId);
            else {
                film = makeFilm(rs);
                data.put(film, new ArrayList<>());
                mapId.put(film.getId(), film);
            }
            if (genre != null) {
                data.get(film).add(genre);
            }
        }
        return data;
    }

}
