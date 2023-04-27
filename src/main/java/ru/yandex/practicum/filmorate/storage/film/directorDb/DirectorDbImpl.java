package ru.yandex.practicum.filmorate.storage.film.directorDb;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Director;

import java.util.*;


@Repository
@RequiredArgsConstructor
public class DirectorDbImpl implements DirectorDb {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    private final RowMapper<Director> directorMapper;

    @Override
    public Director createDirector(Director director) {
        String sql = "INSERT INTO DIRECTORS (DIRECTOR_NAME) " +
                "VALUES (:DIRECTOR_NAME); ";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        Map<String, Object> params = Collections.singletonMap("DIRECTOR_NAME", director.getName());
        SqlParameterSource paramSource = new MapSqlParameterSource(params);
        jdbcTemplate.update(sql, paramSource, keyHolder);
        director.setId(keyHolder.getKey().longValue());
        return director;
    }

    @Override
    public Director update(Director director) {
        String sql = "UPDATE DIRECTORS SET director_name = :director_name " +
                "WHERE director_id = :director_id";
        Map<String, Object> params = new HashMap<>();
        params.put("director_id", director.getId());
        params.put("director_name", director.getName());
        SqlParameterSource paramSource = new MapSqlParameterSource(params);
        if (jdbcTemplate.update(sql, paramSource) == 0)
            throw new ValidationException("Пользователя не существует");
        return director;
    }

    @Override
    public Director getDirector(long id) {
        String sql = "SELECT * FROM DIRECTORS WHERE DIRECTOR_ID = :DIRECTOR_ID; ";
        var params = Collections.singletonMap("DIRECTOR_ID", id);
        try {
            return jdbcTemplate.queryForObject(sql, params, directorMapper);
        } catch (RuntimeException e) {
            throw new ValidationException("Режиссёр с таким id не существует");
        }

    }

    @Override
    public List<Director> findDirectorFilm(long filmId) {
        String sql = "SELECT * FROM director_films df INNER JOIN directors d ON d.director_id = df.director_id  " +
                "WHERE film_id = :film_id";
        Map<String, Object> params = Collections.singletonMap("film_id", filmId);
        return jdbcTemplate.query(sql, params, directorMapper);
    }

    @Override
    public List<Director> findAll() {
        String sql = "SELECT * FROM DIRECTORS";

        return jdbcTemplate.query(sql, directorMapper);

    }

    @Override
    public List<Director> addDirectorToFilm(long filmId, List<Director> directors) {
        if (directors == null) return new ArrayList<>();
        var countDirectors = directors.size();
        String sqlGenreInfo = "MERGE INTO director_films(film_id, director_id) VALUES (:film_id, :director_id);";
        SqlParameterSource[] sources = new SqlParameterSource[countDirectors];
        for (int i = 0; i < countDirectors; i++) {
            Map<String, Number> params = new HashMap<>();
            params.put("film_id", filmId);
            params.put("director_id", directors.get(i).getId());
            sources[i] = new MapSqlParameterSource(params);
        }

        try {
            jdbcTemplate.batchUpdate(sqlGenreInfo, sources);
        } catch (RuntimeException e) {
            throw new ValidationException("Режиссёр с таким id не существует");
        }
        return findDirectorFilm(filmId);
    }

    @Override
    public List<Director> updateDirectorFilm(long filmId, List<Director> genres) {
        String sql = "DELETE FROM director_films WHERE film_id = :film_id;";
        Map<String, Object> params = Collections.singletonMap("film_id", filmId);
        jdbcTemplate.update(sql, params);
        return addDirectorToFilm(filmId, genres);
    }

    @Override
    public Void deleteDirector(long id) {
        String sql = "DELETE FROM director_films WHERE director_id = :director_id;";
        String sqlDelDirector = "DELETE FROM directors WHERE director_id = :director_id;";
        Map<String, Object> params = Collections.singletonMap("director_id", id);
        jdbcTemplate.update(sql, params);
        jdbcTemplate.update(sqlDelDirector, params);


        return null;
    }
}
