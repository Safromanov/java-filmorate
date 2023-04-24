package ru.yandex.practicum.filmorate.storage.film.GenreDAO;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
@RequiredArgsConstructor
public class GenreDBImpl implements GenreDB {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    private final RowMapper<Genre> genreMapper;

    public Genre getGenre(int id) {
        String sql = "SELECT * FROM genre WHERE genre_id = :genre_id;";

        Map<String, Object> params = Collections.singletonMap("genre_id", id);
        try {
            return jdbcTemplate.queryForObject(sql, params, genreMapper);
        } catch (Exception e) {
            throw new ValidationException("Жанр с таким id отсутствует");
        }
    }

    public List<Genre> findAll() {
        String sql = "SELECT * FROM genre";
        return jdbcTemplate.query(sql, genreMapper);
    }

    public List<Genre> findGenresFilm(long filmId) {
        String sql = "SELECT * FROM GENRE_FILMS gf INNER JOIN genre g ON g.genre_id = gf.genre_id  " +
                "WHERE film_id = :film_id";
        Map<String, Object> params = Collections.singletonMap("film_id", filmId);
        return jdbcTemplate.query(sql, params, genreMapper);
    }

    public List<Genre> addGenresToFilm(long filmId, List<Genre> genres) {
        String sqlGenreInfo = "MERGE INTO genre_films(film_id, genre_id) VALUES (:film_id, :genre_id);";
        Map<String, Object> params = new HashMap<>();
        params.put("film_id", filmId);

        if (genres != null)
            for (var genre : genres) {
                params.put("genre_id", genre.getId());
                jdbcTemplate.update(sqlGenreInfo, params);
            }
        return findGenresFilm(filmId);
    }

    public List<Genre> updateGenresFilm(long filmId, List<Genre> genres) {
        String sql = "DELETE FROM GENRE_FILMS WHERE film_id = :film_id;";
        Map<String, Object> params = Collections.singletonMap("film_id", filmId);
        jdbcTemplate.update(sql, params);
        return addGenresToFilm(filmId, genres);
    }

}

