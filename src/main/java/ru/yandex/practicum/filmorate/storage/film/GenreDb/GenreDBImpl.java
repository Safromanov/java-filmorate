package ru.yandex.practicum.filmorate.storage.film.GenreDb;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.*;
import java.util.stream.Collectors;

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

    public Set<Genre> findGenresFilm(long filmId) {
        String sql = "SELECT * FROM GENRE_FILMS gf INNER JOIN genre g ON g.genre_id = gf.genre_id  " +
                "WHERE film_id = :film_id";
        Map<String, Object> params = Collections.singletonMap("film_id", filmId);
        return jdbcTemplate.queryForStream(sql, params, genreMapper)
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    public Set<Genre> addGenresToFilm(long filmId, Set<Genre> genres) {
        if (genres == null) return new HashSet<>();
        var countGenres = genres.size();
        String sqlGenreInfo = "INSERT INTO genre_films(film_id, genre_id) VALUES (:film_id, :genre_id);";
        SqlParameterSource[] sources = new SqlParameterSource[countGenres];
        for (var genre : genres) {
            Map<String, Number> params = new HashMap<>();
            params.put("film_id", filmId);
            params.put("genre_id", genre.getId());
            sources[--countGenres] = new MapSqlParameterSource(params);
        }
        jdbcTemplate.batchUpdate(sqlGenreInfo, sources);
        return findGenresFilm(filmId);
    }

    public Set<Genre> updateGenresFilm(long filmId, Set<Genre> genres) {
        String sql = "DELETE FROM GENRE_FILMS WHERE film_id = :film_id;";
        Map<String, Object> params = Collections.singletonMap("film_id", filmId);
        jdbcTemplate.update(sql, params);
        return addGenresToFilm(filmId, genres);
    }

}

