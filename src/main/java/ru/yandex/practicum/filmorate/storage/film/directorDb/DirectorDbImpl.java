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

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


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
        return null;
    }

    @Override
    public List<Director> findAll() {
        String sql = "SELECT * FROM DIRECTORS";

        return jdbcTemplate.query(sql, directorMapper);

    }

    @Override
    public List<Director> addDirectorToFilm(long filmId, List<Director> genres) {
        return null;
    }

    @Override
    public List<Director> updateDirectorFilm(long filmId, List<Director> genres) {
        return null;
    }

    @Override
    public Void deleteDirector(long id) {
        return null;
    }
}
