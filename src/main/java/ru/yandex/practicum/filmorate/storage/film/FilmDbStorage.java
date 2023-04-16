package ru.yandex.practicum.filmorate.storage.film;

import lombok.AllArgsConstructor;

import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;

import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;


import java.sql.*;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static ru.yandex.practicum.filmorate.model.Film.makeFilm;


@Primary
@Component
@AllArgsConstructor
public class FilmDbStorage implements FilmStorage {

    private final JdbcTemplate jdbcTemplate;

    private final GenreDB genreDB;

    @Override
    public Collection<Film> findAll() {
        String sql = "SELECT * FROM films";
        RowMapper<Film> rowMapper = (resultSet, rowNum) -> makeFilm(resultSet);
        PreparedStatementCreator preparedStatementCreator = con -> con.prepareStatement(sql, new String[]{"film_id"});
        var films = jdbcTemplate.query(
                preparedStatementCreator,
                rowMapper);
        for (var film : films) {
            film.setGenres(genreDB.findGenresFilm(film.getId()));
        }
        return films;
    }

    @Override
    public Film create(Film film) {
        String sql = "INSERT INTO films (FILM_NAME,  DESCRIPTION, RELEASE_DATE, DURATION_MINUTE, MPA_id) " +
                "VALUES (?,?,?,?,?);";

        KeyHolder keyHolder = new GeneratedKeyHolder();

        PreparedStatementCreator preparedStatementCreator = con -> makeStatement(con, film, sql);

        jdbcTemplate.update(preparedStatementCreator, keyHolder);

        film.setGenres(genreDB.create(keyHolder.getKey().longValue(), film.getGenres()));

        film.setId(keyHolder.getKey().longValue());
        return film;
    }

    @Override
    public Film update(Film film) {

        String sql = "UPDATE films SET FILM_NAME=?, DESCRIPTION=?, RELEASE_DATE=?, DURATION_MINUTE=?, MPA_id=?" +
                "WHERE film_id =?;";

        KeyHolder keyHolder = new GeneratedKeyHolder();

        PreparedStatementCreator preparedStatementCreator = con -> {
            var stmt = makeStatement(con, film, sql);
            stmt.setString(6, String.valueOf(film.getId()));
            return stmt;
        };
        if (jdbcTemplate.update(preparedStatementCreator, keyHolder) == 0)
            throw new ValidationException("Фильма не существует");
        film.setGenres(genreDB.update(film.getId(), film.getGenres()));
        return film;

    }

    @Override
    public Film getFilm(long id) {
        String sql = "SELECT * FROM films WHERE film_id = ?;";
        Collection<Genre> genres = genreDB.findGenresFilm(id);
        RowMapper<Film> rowMapper = (resultSet, rowNum) -> makeFilm(resultSet, genres);
        try {
            return jdbcTemplate.queryForObject(
                    sql, rowMapper, id);
        } catch (Exception e) {
            throw new ValidationException("Фильма не существует");
        }
    }

    @Override
    public void addLike(long filmId, long userId) {
        String sql = "MERGE INTO likes_film VALUES (?, ?)";
        jdbcTemplate.update(sql, filmId, userId);
    }

    @Override
    public void removeLike(long userId, long filmId) {
        String sql = "DELETE FROM likes_film WHERE user_id = ? AND film_id = ?";
        if (jdbcTemplate.update(sql, userId, filmId) == 0)
            throw new ValidationException("Введён несуществующий id");
    }

    @Override
    public List<Film> getPopularFilm(int size) {
        String sql = "\nSELECT f.film_id, f.FILM_NAME, f.DESCRIPTION, f.MPA_ID, f.RELEASE_DATE, f.DURATION_MINUTE\n" +
                "from\n" +
                "(SELECT film_id FROM likes_film\n" +
                "GROUP BY film_id\n" +
                "ORDER by COUNT(user_id) DESC\n" +
                "LIMIT ?) popular_film\n" +
                "LEFT JOIN films f ON f.film_id = popular_film.film_id\n";
        RowMapper<Film> rowMapper = (resultSet, rowNum) -> makeFilm(resultSet);

        PreparedStatementCreator preparedStatementCreator = con -> {
            PreparedStatement stmt = con.prepareStatement(sql, new String[]{"film_id"});
            stmt.setString(1, String.valueOf(size));
            return stmt;
        };

        var listPopular = jdbcTemplate.query(
                preparedStatementCreator,
                rowMapper);
        if (listPopular.isEmpty())
            listPopular = findAll().stream().limit(size).collect(Collectors.toList());
        return listPopular;
    }

    private PreparedStatement makeStatement(Connection con, Film film, String sql) throws SQLException {
        PreparedStatement stmt = con.prepareStatement(sql, new String[]{"film_id"});
        stmt.setString(1, film.getName());
        stmt.setString(2, film.getDescription());
        if (film.getMpa() != null)
            stmt.setString(5, String.valueOf(film.getMpa().getId()));
        stmt.setString(3, film.getReleaseDate().toString());
        stmt.setString(4, String.valueOf(film.getDuration().getSeconds()));
        return stmt;
    }

}
