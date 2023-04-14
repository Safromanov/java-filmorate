package ru.yandex.practicum.filmorate.storage.film;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.Collection;

import static ru.yandex.practicum.filmorate.model.Genre.makeGenre;

@Component
@RequiredArgsConstructor
public class GenreDB {
    private final JdbcTemplate jdbcTemplate;

    public Genre getGenre(int id) {
        String sql = "SELECT * FROM genre WHERE genre_id = ?;";
        RowMapper<Genre> rowMapper = (resultSet, rowNum) -> makeGenre(resultSet);
        try {
            return jdbcTemplate.queryForObject(
                    sql,rowMapper, id);
        } catch (Exception e) {
            throw new ValidationException("Этого жанра нет в базе =(");
        }
    }


    public Collection<Genre> findAll() {
        String sql = "SELECT * FROM genre";
        RowMapper<Genre> rowMapper = (resultSet, rowNum) -> makeGenre(resultSet);
        PreparedStatementCreator preparedStatementCreator = con -> con.prepareStatement(sql, new String[]{"genre_id"});
        return jdbcTemplate.query(
                preparedStatementCreator,
                rowMapper);
    }


}
