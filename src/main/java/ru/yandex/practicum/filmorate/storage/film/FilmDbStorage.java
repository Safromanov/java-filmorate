package ru.yandex.practicum.filmorate.storage.film;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.*;
import java.util.stream.Collectors;

@Primary
@Repository
@RequiredArgsConstructor
public class FilmDbStorage implements FilmStorage {

    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final ResultSetExtractor<Map<Film, List<Genre>>> filmExtractor;

    private final RowMapper<Director> dirMapper;

    @Override
    public Collection<Film> findAll() {
        String sqlGetAllFilms = "SELECT * FROM films f \n" +
                "LEFT JOIN genre_films gf ON f.film_id = gf.film_id \n" +
                "LEFT JOIN genre g ON gf.genre_id = g.genre_id " +
                "LEFT JOIN director_films df ON f.film_id = df.film_id \n" +
                "LEFT JOIN directors d ON df.director_id = d.director_id ;";
        var mapGenre = jdbcTemplate.query(sqlGetAllFilms, filmExtractor);
        return mapGenre.keySet();
    }

    @Override
    public Film add(Film film) {
        String sqlAddFilm =
                "INSERT INTO films (film_name,  description, release_date, duration_minute, MPA_id) " +
                        "VALUES (:film_name, :description, :release_date, :duration_minute, :MPA_id);";

        Map<String, Object> params = new HashMap<>();
        params.put("film_name", film.getName());
        params.put("description", film.getDescription());
        params.put("release_date", film.getReleaseDate());
        params.put("duration_minute", film.getDuration().getSeconds());
        params.put("MPA_id", film.getMpa().getId());

        KeyHolder keyHolder = new GeneratedKeyHolder();
        SqlParameterSource paramSource = new MapSqlParameterSource(params);
        jdbcTemplate.update(sqlAddFilm, paramSource, keyHolder);

        film.setId(keyHolder.getKey().longValue());
        return film;

    }

    @Override
    public Film update(Film film) {

        String sql = "UPDATE films SET film_name = :film_name, description = :description, release_date = :release_date, " +
                "duration_minute = :duration_minute, MPA_id = :MPA_id " +
                "WHERE film_id =:film_id;";

        Map<String, Object> params = new HashMap<>();
        params.put("film_name", film.getName());
        params.put("description", film.getDescription());
        params.put("release_date", film.getReleaseDate());
        params.put("duration_minute", film.getDuration().getSeconds());
        params.put("MPA_id", film.getMpa().getId());
        params.put("film_id", film.getId());
        if (jdbcTemplate.update(sql, params) == 0)
            throw new ValidationException("Фильма не существует");

        return film;
    }

    @Override
    public Film getFilm(long id) {
        String sqlGetFilm = "SELECT * FROM films f \n" +
                "LEFT JOIN genre_films gf ON f.film_id = gf.film_id \n" +
                "LEFT JOIN genre g ON gf.genre_id = g.genre_id \n" +
                "LEFT JOIN director_films df ON f.film_id = df.film_id \n" +
                "LEFT JOIN directors d ON df.director_id = d.director_id \n" +
                "WHERE f.film_id = :film_id ;";
        var params = Collections.singletonMap("film_id", id);
        var film = jdbcTemplate.query(sqlGetFilm, params, filmExtractor).keySet().stream().findAny();
        if (film.isPresent())
            return film.get();
        else throw new ValidationException("Неверный id");
    }

    @Override
    public void addLike(long filmId, long userId) {
        String sql = "MERGE INTO likes_film (film_id, user_id) VALUES (:film_id, :user_id)";
        Map<String, Object> params = new HashMap<>();
        params.put("film_id", filmId);
        params.put("user_id", userId);
        jdbcTemplate.update(sql, params);
    }

    @Override
    public void removeLike(long userId, long filmId) {
        String sql = "DELETE FROM likes_film WHERE user_id = :user_id AND film_id = :film_id";
        Map<String, Object> params = new HashMap<>();
        params.put("film_id", filmId);
        params.put("user_id", userId);
        if (jdbcTemplate.update(sql, params) == 0)
            throw new ValidationException("Введён несуществующий id");
    }

    @Override
    public Collection<Film> createCollectionFilmsById(Collection<Long> filmsId) {
        Collection<Film> films = new ArrayList<>();
        for (Long id : filmsId) {
            films.add(getFilm(id));
        }
        return films;
    }

    @Override
    public Set<Film> getPopularFilm(int size) {
        String sqlGetPopularFilms = "\nSELECT * \n" +
                "FROM \n" +
                "(SELECT film_id FROM likes_film \n" +
                "GROUP BY film_id \n" +
                "ORDER BY COUNT(user_id) DESC \n" +
                "LIMIT :size) popular_film \n" +
                "LEFT JOIN films f ON f.film_id = popular_film.film_id \n" +
                "LEFT JOIN genre_films gf ON f.film_id = gf.film_id \n" +
                "LEFT JOIN genre g ON gf.genre_id = g.genre_id \n" +
                "LEFT JOIN director_films df ON f.film_id = df.film_id \n" +
                "LEFT JOIN directors d ON df.director_id = d.director_id \n;";
        var param = Collections.singletonMap("size", size);
        var listPopular = jdbcTemplate.query(sqlGetPopularFilms, param, filmExtractor).keySet();
        if (listPopular.isEmpty())
            listPopular = findAll().stream().limit(size).collect(Collectors.toSet());
        return listPopular;
    }

    public Collection<Film> getSortFilmsByDirector(long id, String sortBy) {

        String sqlCheckDir = "SELECT * FROM directors WHERE director_id = :director_id";


        String sqlYearSort = "\nSELECT * \n" +
                "FROM \n" +
                "(SELECT film_id, director_id FROM director_films\n" +
                "WHERE director_id =  :director_id" +
                ") film_by_director\n" +
                "LEFT JOIN films f ON f.film_id = film_by_director.film_id \n" +
                "LEFT JOIN genre_films gf ON f.film_id = gf.film_id \n" +
                "LEFT JOIN genre g ON gf.genre_id = g.genre_id \n" +
                "LEFT JOIN directors d ON film_by_director.director_id = d.director_id\n" +
                "ORDER BY YEAR(f.release_date);";

        String sqlLikesSort = "\n" +
                "SELECT * FROM " +
                " director_films df\n" +
                "LEFT JOIN films f ON f.film_id = df.film_id \n" +
                "LEFT JOIN genre_films gf ON f.film_id = gf.film_id \n" +
                "LEFT JOIN genre g ON gf.genre_id = g.genre_id \n" +
                "LEFT JOIN directors d ON df.director_id = d.director_id\n" +
                "LEFT JOIN likes_film lf ON f.film_id = lf.film_id\n" +
                "WHERE df.director_id =  :director_id\n" +
                "group by df.film_id\n" +
                "ORDER BY COUNT(lf.USER_ID);\n";

        var param = Collections.singletonMap("director_id", id);

        if (jdbcTemplate.query(sqlCheckDir, param, dirMapper).size() == 0)
            throw new ValidationException("Режиссера не существует");

        if (Objects.equals(sortBy, "year")) return jdbcTemplate.query(sqlYearSort, param, filmExtractor).keySet();
        if (Objects.equals(sortBy, "likes")) return jdbcTemplate.query(sqlLikesSort, param, filmExtractor).keySet();


        throw new ValidationException("Неподдерживаемый параметр сортировки");
    }

}
