package ru.yandex.practicum.filmorate.storage.film;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.MPA;


import java.sql.*;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static ru.yandex.practicum.filmorate.model.Film.makeFilm;


@Primary
@Component
@AllArgsConstructor
public class FilmDbStorage implements  FilmStorage{
    JdbcTemplate jdbcTemplate;
   // SimpleJdbcInsert simpleJdbcInsert;
    @Override
    public Collection<Film> findAll() {
        String sql = "SELECT * FROM films";
        RowMapper<Film> rowMapper = (resultSet, rowNum) -> makeFilm(resultSet);
        PreparedStatementCreator preparedStatementCreator = con -> con.prepareStatement(sql, new String[]{"film_id"});
        return jdbcTemplate.query(
                preparedStatementCreator,
                rowMapper);
    }

    @Override
    public Film create(Film film) {
        System.out.println(film.getMpa());
//        String sql = "INSERT INTO films (FILM_NAME,  DESCRIPTION, MPA_ID, RELEASE_DATE, DURATION_MINUTE) " +
//                "VALUES (?,?,?,?,?);";
        String sql = "INSERT INTO films (FILM_NAME,  DESCRIPTION, RELEASE_DATE, DURATION_MINUTE) " +
                "VALUES (?,?,?,?);";
    //    System.out.println(film.getMpa().getId());
        KeyHolder keyHolder = new GeneratedKeyHolder();

        PreparedStatementCreator preparedStatementCreator = con -> makeStatement(con, film, sql);

        jdbcTemplate.update(preparedStatementCreator, keyHolder);
        film.setId(keyHolder.getKey().longValue());
        return film;
    }

    @Override
    public Film update(Film film) {
//        String sql = "UPDATE films SET FILM_NAME=?, DESCRIPTION=?, MPA_ID=?, GENRE_ID=? , RELEASE_DATE=?, DURATION_MINUTE=?" +
//                "WHERE film_id =?;";

        var genreId = 1;// Нужно сходить в таблицу жанров и записать что такой фильв в такой таблице

        String sql = "UPDATE films SET FILM_NAME=?, DESCRIPTION=?, MPA_ID=?, RELEASE_DATE=?, DURATION_MINUTE=?" +
                "WHERE film_id =?;";

        String sqlToGenreFilms = "MERGE genre_films SET FILM_NAME=?, FILM_NAME=?" +
                "WHERE film_id =?;";

        KeyHolder keyHolder = new GeneratedKeyHolder();

        PreparedStatementCreator preparedStatementCreator = con -> {
            var stmt = makeStatement(con, film, sql);
            stmt.setString(6, String.valueOf(film.getId()));
            stmt.setString(1, String.valueOf(film.getName()));
            stmt.setString(2, String.valueOf(film.getDescription()));
            stmt.setString(3, String.valueOf(film.getMpa().getId()));
//            stmt.setString(7, String.valueOf(film.getGenres()));
            stmt.setString(4, Date.valueOf(film.getReleaseDate()).toString());
            stmt.setString(5, String.valueOf(film.getDuration().toMinutes()));
            System.out.println(stmt);
            return stmt;
        };

        try {
            jdbcTemplate.update(preparedStatementCreator, keyHolder);
        } catch (Exception e) {
            throw new ValidationException("Фильма не существует");
        }
        return film;
    }

    @Override
    public Film getFilm(long id) {
        String sql = "SELECT * FROM films WHERE film_id = ?;";
        RowMapper<Film> rowMapper = (resultSet, rowNum) -> makeFilm(resultSet);

//        PreparedStatementCreator preparedStatementCreator = con -> {
//            PreparedStatement stmt = con.prepareStatement(sql, new String[]{"film_id"});
//            stmt.setString(1, String.valueOf(id));
//            return stmt;
//        };

        try {
            return jdbcTemplate.queryForObject(
                    sql,rowMapper, id);
        } catch (Exception e) {
            throw new ValidationException("Фильма не существует");
        }
    }

    @Override
    public List<Film> getPopularFilm(int size) {
        return null;
    }

    private String findIdGenre(String genre){
        String sql = "SELECT genre_id FROM films WHERE genre_name = ?";
        RowMapper<Integer> rowMapper = (resultSet, rowNum) -> resultSet.findColumn("genre_id");

        PreparedStatementCreator preparedStatementCreator = con -> {
            PreparedStatement stmt = con.prepareStatement(sql, new String[]{"genre_id"});
            stmt.setString(1,genre);
            return stmt;
        };

        try {
            return jdbcTemplate.query(
                    preparedStatementCreator,
                    rowMapper).get(0).toString();
        } catch (Exception e) {
            jdbcTemplate.update(
                    "INSERT INTO genre (genre_name) VALUES (?);", genre);// Тут нужно создать новый жанр в таблице
            return findIdGenre(genre);
        }
    }

//    private String findIdMPA(MPA mpa){
//        String sql = "SELECT mpa_id FROM mpa WHERE mpa_name = ?";
//        RowMapper<Integer> rowMapper = (resultSet, rowNum) -> resultSet.findColumn("mpa_id");
//
//        PreparedStatementCreator preparedStatementCreator = con -> {
//            PreparedStatement stmt = con.prepareStatement(sql, new String[]{"mpa_id"});
//            stmt.setString(1, mpa.getMpa());
//            return stmt;
//        };
//
//        try {
//            return jdbcTemplate.query(
//                    preparedStatementCreator,
//                    rowMapper).get(0).toString();
//        } catch (Exception e) {
//            throw new ValidationException("Такой категории нет");
//        }
//    }

    private PreparedStatement makeStatement(Connection con, Film film, String sql) throws SQLException {
        PreparedStatement stmt = con.prepareStatement(sql, new String[]{"film_id"});
        stmt.setString(1, film.getName());
        stmt.setString(2, film.getDescription());
//        if (film.getMpaId() != null)
//             stmt.setString(4, String.valueOf(film.getMpaId()));
//        else stmt.setString(4, null);
//        stmt.setString(4, findIdGenre(film.getGenre()));
        stmt.setString(3, film.getReleaseDate().toString());
        stmt.setString(4, String.valueOf(film.getDuration().toMinutes()));
        return stmt;
    }

}
