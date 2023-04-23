package ru.yandex.practicum.filmorate.storage.film;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Primary
@Repository
@RequiredArgsConstructor
public class FilmDbStorage implements FilmStorage {

    private final JdbcTemplate jdbcTemplate;

    private final ResultSetExtractor<Map<Film, List<Genre>>> filmExtractor;

    @Override
    public Collection<Film> findAll() {
        String sqlGetAllFilms = "\nSELECT *\n" +
                "FROM films f \n" +
                "LEFT JOIN genre_films gf ON f.film_id = gf.film_id \n" +
                "LEFT JOIN genre g ON gf.genre_id = g.genre_id";
        var mapGenre = jdbcTemplate.query(sqlGetAllFilms, filmExtractor);
        for (var film : mapGenre.entrySet()) {
            film.getKey().setGenres(film.getValue());
        }
        return mapGenre.keySet();
    }

    @Override
    public Film add(Film film) {
        String sqlAddFilm =
                "INSERT INTO films (FILM_NAME,  DESCRIPTION, RELEASE_DATE, DURATION_MINUTE, MPA_id) " +
                        "VALUES (?,?,?,?,?);";

        KeyHolder keyHolder = new GeneratedKeyHolder();

        PreparedStatementCreator preparedStatementCreator = con -> makeStatement(con, film, sqlAddFilm);

        jdbcTemplate.update(preparedStatementCreator, keyHolder);

        film.setId(keyHolder.getKey().longValue());

        return film;

    }

    @Override
    public Film update(Film film) {

        String sql = "UPDATE films SET FILM_NAME=?, DESCRIPTION=?, RELEASE_DATE=?, DURATION_MINUTE=?, MPA_id=?" +
                "WHERE film_id =?;";

        PreparedStatementCreator preparedStatementCreator = con -> {
            var stmt = makeStatement(con, film, sql);
            stmt.setString(6, String.valueOf(film.getId()));
            return stmt;
        };

        if (jdbcTemplate.update(preparedStatementCreator) == 0)
            throw new ValidationException("Фильма не существует");

        return film;
    }

    @Override
    public Film getFilm(long id) {
        String sqlGetFilm = "\nSELECT * \n" +
                "FROM films f\n" +
                "LEFT JOIN genre_films gf ON f.film_id = gf.film_id \n" +
                "LEFT JOIN genre g ON gf.genre_id = g.genre_id \n" +
                "WHERE f.film_id = ?";
        var mapGenre = jdbcTemplate.query(sqlGetFilm, filmExtractor, id);
        for (var film : mapGenre.entrySet()) {
            film.getKey().setGenres(film.getValue());
            return film.getKey();
        }
        throw new ValidationException("Неверный id");
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
    public Set<Film> getPopularFilm(int size) {
        String sqlGetPopularFilms = "\nSELECT * \n" +
                "FROM \n" +
                "(SELECT film_id FROM likes_film \n" +
                "GROUP BY film_id \n" +
                "ORDER by COUNT(user_id) DESC \n" +
                "LIMIT ?) popular_film \n" +
                "LEFT JOIN films f ON f.film_id = popular_film.film_id \n" +
                "LEFT JOIN genre_films gf ON f.film_id = gf.film_id \n" +
                "LEFT JOIN genre g ON gf.genre_id = g.genre_id";
        var mapGenre = jdbcTemplate.query(sqlGetPopularFilms, filmExtractor, size);
        for (var film : mapGenre.entrySet()) {
            film.getKey().setGenres(film.getValue());
        }
        var listPopular = mapGenre.keySet();
        if (listPopular.isEmpty())
            listPopular = findAll().stream().limit(size).collect(Collectors.toSet());
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
