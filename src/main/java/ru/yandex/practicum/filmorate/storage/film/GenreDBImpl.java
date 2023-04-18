package ru.yandex.practicum.filmorate.storage.film;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Genre;

import java.sql.PreparedStatement;
import java.util.List;

import static ru.yandex.practicum.filmorate.model.Genre.makeGenre;

@Repository
@RequiredArgsConstructor
public class GenreDBImpl implements GenreDB {

    private final JdbcTemplate jdbcTemplate;

    public Genre getGenre(int id) {
        String sql = "SELECT * FROM genre WHERE genre_id = ?;";
        RowMapper<Genre> rowMapper = (resultSet, rowNum) -> makeGenre(resultSet);
        try {
            return jdbcTemplate.queryForObject(
                    sql, rowMapper, id);
        } catch (Exception e) {
            throw new ValidationException("Этого жанра нет в базе =(");
        }
    }

    public List<Genre> findAll() {
        String sql = "SELECT * FROM genre";
        RowMapper<Genre> rowMapper = (resultSet, rowNum) -> makeGenre(resultSet);
        PreparedStatementCreator preparedStatementCreator = con -> con.prepareStatement(sql, new String[]{"genre_id"});
        return jdbcTemplate.query(
                preparedStatementCreator,
                rowMapper);
    }

    public List<Genre> findGenresFilm(long filmId) {
        String sql = "SELECT * FROM GENRE_FILMS gf INNER JOIN genre g ON g.genre_id = gf.genre_id  WHERE film_id = ?";
        RowMapper<Genre> rowMapper = (resultSet, rowNum) -> makeGenre(resultSet);
        PreparedStatementCreator preparedStatementCreator = con -> {
            PreparedStatement stmt = con.prepareStatement(sql, new String[]{"genre_id", "film_id"});
            stmt.setString(1, String.valueOf(filmId));
            return stmt;
        };
        return jdbcTemplate.query(
                preparedStatementCreator,
                rowMapper);
    }

    public List<Genre> addGenresToFilm(long filmId, List<Genre> genres) {
        String sqlGenreInfo = "MERGE INTO genre_films(film_id, genre_id) VALUES (?,?);";
        if (genres != null)
            for (var genre : genres)
                jdbcTemplate.update(sqlGenreInfo, filmId, genre.getId());
        return findGenresFilm(filmId);
    }

    public List<Genre> updateGenresFilm(long filmId, List<Genre> genres) {
        String sql = "DELETE GENRE_FILMS WHERE film_id = ?;";
        jdbcTemplate.update(sql, filmId);
        return addGenresToFilm(filmId, genres);
    }

}

