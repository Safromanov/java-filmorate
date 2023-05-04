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
import ru.yandex.practicum.filmorate.storage.film.mappers.FilmMapper;
import ru.yandex.practicum.filmorate.storage.film.mappers.ValuesExtractor;

import java.util.*;
import java.util.stream.Collectors;


@Primary
@Repository
@RequiredArgsConstructor
public class FilmDbStorage implements FilmStorage {

    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final ResultSetExtractor<Set<Film>> filmExtractor;
    private final FilmMapper filmMapper;
    private final RowMapper<Director> dirMapper;
    private final RowMapper<Genre> genreMapper;

    @Override
    public Collection<Film> findAll() {
        String sqlGetAllFilms = "SELECT * FROM films f ORDER BY FILM_ID \n";
        String sqlGetAllGenres = "SELECT * FROM genre_films gf \n"
                + "LEFT JOIN genre g ON gf.genre_id = g.genre_id ";
        String sqlGetAllDirectors = "SELECT * FROM director_films df \n"
                + "LEFT JOIN directors d ON df.director_id = d.director_id ";
        return getFilmsByParams(sqlGetAllFilms, sqlGetAllDirectors, sqlGetAllGenres, null);
        //   var mapGenre = jdbcTemplate.query(sqlGetAllFilms, filmExtractor);
        //  return mapGenre;
    }

    @Override
    public Film add(Film film) {
        String sqlAddFilm = "INSERT INTO films (film_name,  description, release_date, duration_minute, MPA_id) "
                + "VALUES (:film_name, :description, :release_date, :duration_minute, :MPA_id);";

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

        String sql = "UPDATE films SET film_name = :film_name, description = :description, release_date = :release_date, "
                + "duration_minute = :duration_minute, MPA_id = :MPA_id " + "WHERE film_id =:film_id;";

        Map<String, Object> params = new HashMap<>();
        params.put("film_name", film.getName());
        params.put("description", film.getDescription());
        params.put("release_date", film.getReleaseDate());
        params.put("duration_minute", film.getDuration().getSeconds());
        params.put("MPA_id", film.getMpa().getId());
        params.put("film_id", film.getId());
        if (jdbcTemplate.update(sql, params) == 0) throw new ValidationException("Фильма не существует");

        return film;
    }

//    @Override
//    public Film getFilm(long id) {
//        String sqlGetFilm = "SELECT * FROM films f \n"
//                + "LEFT JOIN genre_films gf ON f.film_id = gf.film_id \n"
//                + "LEFT JOIN genre g ON gf.genre_id = g.genre_id \n"
//                + "LEFT JOIN director_films df ON f.film_id = df.film_id \n"
//                + "LEFT JOIN directors d ON df.director_id = d.director_id \n"
//                + "WHERE f.film_id = :film_id ;";
//
//        var params = Collections.singletonMap("film_id", id);
//        var film = jdbcTemplate.query(sqlGetFilm, params, filmExtractor).stream().findAny();
//        if (film.isPresent()) return film.get();
//        else throw new ValidationException("Неверный id");
//    }

    @Override
    public Film getFilm(long id) {
        String sqlGetFilm = "SELECT * FROM films f WHERE f.film_id = :film_id ;";

        String sqlGetDir = "SELECT * FROM DIRECTOR_FILMS df " +
                "LEFT JOIN  directors d on d.director_id = df.director_id " +
                "WHERE df.film_id = :film_id ;";

        String sqlGetGenre = "SELECT * FROM genre_films gf " +
                "LEFT JOIN  genre g on g.genre_id = gf.genre_id " +
                "WHERE gf.film_id = :film_id ;";

        var params = Collections.singletonMap("film_id", id);
        try {
            var film = Optional.of(jdbcTemplate.queryForObject(sqlGetFilm, params, filmMapper));
            var filmId = film.get().getId();
            var dir = jdbcTemplate.query(sqlGetDir, params, new ValuesExtractor<>(dirMapper));
            if (dir.get(filmId) != null) film.get().setDirectors(dir.get(film.get().getId()));
            var gen = jdbcTemplate.query(sqlGetGenre, params, new ValuesExtractor<>(genreMapper));
            if (gen.get(filmId) != null) film.get().setGenres(gen.get(film.get().getId()));
            return film.get();
        } catch (RuntimeException e) {
            throw new ValidationException("Несуществующий id");
        }
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
        if (jdbcTemplate.update(sql, params) == 0) throw new ValidationException("Введён несуществующий id");
    }

    @Override
    public List<Film> createListFilmsById(Collection<Long> filmsId) {
        List<Film> films = new ArrayList<>();
        for (Long id : filmsId) {
            films.add(getFilm(id));
        }
        return films;
    }

    @Override
    public void deleteFilm(long filmId) {
        getFilm(filmId);
        jdbcTemplate.getJdbcTemplate().update("DELETE FROM likes_film WHERE film_id = ?", filmId);
        jdbcTemplate.getJdbcTemplate().update("DELETE FROM genre_films WHERE film_id = ?", filmId);
        jdbcTemplate.getJdbcTemplate().update("DELETE FROM reviews WHERE film_id = ?", filmId);
        jdbcTemplate.getJdbcTemplate().update("DELETE FROM films WHERE film_id = ?", filmId);
    }

    @Override
    public Set<Film> getPopularFilm(int size, Integer genreId, Integer year) {
        String sqlFilm1 = "SELECT *\n"
                + "FROM (\n"
                + "SELECT f.film_id\n"
                + "FROM FILMS f\t\n"
                + "LEFT JOIN LIKES_FILM lf ON f.film_id = lf.film_id \n";
        String years = "\tEXTRACT(YEAR FROM cast(f.RELEASE_DATE AS date)) IN (:year)\n";
        String genres = "\tf.FILM_ID IN (\n"
                + "\tSELECT gf.FILM_ID\n"
                + "\tFROM GENRE_FILMS gf\n"
                + "\tWHERE  gf.GENRE_ID = :genreId)\n";
        String sqlFilm2 = "GROUP BY f.film_id\n"
                + "ORDER BY COUNT(lf.user_id) DESC, f.film_id\n" + "LIMIT  :size \n"
                + ") popular_film\n"
                + "LEFT JOIN films f ON f.film_id = popular_film.film_id\n"
                + "LEFT JOIN genre_films gf ON f.film_id = gf.film_id\n"
                + "LEFT JOIN genre g ON gf.genre_id = g.genre_id\n"
                + "LEFT JOIN director_films df ON f.film_id = df.film_id\n"
                + "LEFT JOIN directors d ON df.director_id = d.director_id \n";

        String sqlGetPopularFilms = sqlFilm1;
        if (year != -1 || genreId != -1) {
            sqlGetPopularFilms += "\tWHERE\n";
            if (year != -1 && genreId == -1) {
                sqlGetPopularFilms += years;
            } else if (year == -1) {
                sqlGetPopularFilms += genres;
            } else {
                sqlGetPopularFilms += years + " AND " + genres;
            }
        }

        sqlGetPopularFilms += sqlFilm2;
        var param = Map.of("size", size, "genreId", genreId, "year", year);
        var listPopular = jdbcTemplate.query(sqlGetPopularFilms, param, filmExtractor);
        if (listPopular.isEmpty()) {
            listPopular = new HashSet<>();
        }
        return listPopular;
    }

    @Override
    public List<Film> getCommonFilms(Integer userId, Integer friendId) {
        String sqlGetCommonFilms = "SELECT *\n"
                + "FROM (\n"
                + "SELECT commonFilms.*\n"
                + "FROM (\n"
                + "\tSELECT filmsUser.*\n"
                + "\tFROM (\n"
                + "\t\tSELECT f.* \n"
                + "\t\tFROM FILMS f \n"
                + "\t\tLEFT JOIN LIKES_FILM lf ON lf.FILM_ID= f.FILM_ID \n"
                + "\t\tWHERE lf.USER_ID  = :userId \n"
                + "\t) filmsUser\n"
                + "\tLEFT JOIN LIKES_FILM lf ON lf.FILM_ID = filmsUser.FILM_ID\n"
                + "\tWHERE lf.USER_ID = :friendId \n"
                + "\t) commonFilms\n"
                + "\tLEFT JOIN  LIKES_FILM lf ON lf.FILM_ID= commonFilms.FILM_ID \n"
                + "\tGROUP BY commonFilms.film_id\n"
                + "\tORDER BY COUNT(lf.USER_ID) DESC, commonFilms.film_id\n"
                + ") popular_film\n"
                + "LEFT JOIN films f ON f.film_id = popular_film.film_id\n"
                + "LEFT JOIN genre_films gf ON f.film_id = gf.film_id\n"
                + "LEFT JOIN genre g ON gf.genre_id = g.genre_id\n"
                + "LEFT JOIN director_films df ON f.film_id = df.film_id\n"
                + "LEFT JOIN directors d ON df.director_id = d.director_id ";
        var param = Map.of("userId", userId, "friendId", friendId);
        var listCommon = jdbcTemplate.query(sqlGetCommonFilms, param, filmExtractor);
        if (listCommon.isEmpty()) {
            listCommon = new HashSet<>();
        }
        return new ArrayList<>(listCommon);
    }

    public List<Film> getSortFilmsByDirector(long id, String sortBy) {

        String sqlCheckDir = "SELECT * FROM directors WHERE director_id = :director_id";

        String filmByDirector = "(SELECT film_id, director_id FROM director_films\n"
                + "WHERE director_id =  :director_id"
                + ") film_by_director\n";

        String sqlYearSortFilmsDir = "\nSELECT * \n" +
                "FROM \n"
                + filmByDirector
                + "LEFT JOIN directors d ON film_by_director.director_id = d.director_id \n";

        String sqlYearSortFilms = "\nSELECT * \n" +
                "FROM \n"
                + filmByDirector
                + "LEFT JOIN films f ON f.film_id = film_by_director.film_id \n"
                + "ORDER BY YEAR(f.release_date);";

        String sqlYearSortFilmsGenres = "\nSELECT * \n" +
                "FROM \n"
                + filmByDirector
                + "LEFT JOIN genre_films gf ON film_by_director.film_id = gf.film_id \n"
                + "LEFT JOIN genre g ON gf.genre_id = g.genre_id \n";


        String sqlLikesSortForFilms = "\nSELECT f.FILM_ID, FILM_NAME, DESCRIPTION, MPA_ID, RELEASE_DATE, DURATION_MINUTE \n" +
                "FROM " + filmByDirector +
                "LEFT JOIN films f ON f.film_id = film_by_director.film_id " +
                "LEFT JOIN likes_film lf ON f.film_id = lf.film_id " +
                "GROUP BY film_by_director.film_id\n" +
                "ORDER BY COUNT(lf.USER_ID);\n";


        Map<String, Object> params = Collections.singletonMap("director_id", id);

        if (jdbcTemplate.query(sqlCheckDir, params, dirMapper).size() == 0)
            throw new ValidationException("Режиссера не существует");

        if (Objects.equals(sortBy, "year")) {
            return getFilmsByParams(sqlYearSortFilms, sqlYearSortFilmsDir, sqlYearSortFilmsGenres, params);
        }

        if (Objects.equals(sortBy, "likes")) {
            return getFilmsByParams(sqlLikesSortForFilms, sqlYearSortFilmsDir, sqlYearSortFilmsGenres, params);
        }

        throw new ValidationException("Неподдерживаемый параметр сортировки");
    }

    private List<Film> getFilmsByParams(String sqlForFilms, String sqlForDirector, String sqlForGenres, Map<String, Object> params) {
        List<Film> films = jdbcTemplate.queryForStream(sqlForFilms, params, filmMapper)
                .collect(Collectors.toList());
        var dir = jdbcTemplate.query(sqlForDirector, params, new ValuesExtractor<>(dirMapper));

        var gen = jdbcTemplate.query(sqlForGenres, params, new ValuesExtractor<>(genreMapper));
        for (var film : films) {
            film.setDirectors(dir.get(film.getId()) == null ? new HashSet<>() : dir.get(film.getId()));
            film.setGenres(gen.get(film.getId()) == null ? new HashSet<>() : gen.get(film.getId()));
        }
        return films;
    }


    @Override
    public Collection<Film> searchFilms(Map<String, String> searchMap) {
        StringBuilder sqlQuery = new StringBuilder();
        sqlQuery.append("SELECT *\n" +
                "FROM (\n" +
                "SELECT f.film_id\n" +
                "FROM FILMS f\t\n" +
                "LEFT JOIN LIKES_FILM lf ON f.film_id = lf.film_id \n");
        if (searchMap.containsKey("title") || searchMap.containsKey("director")) {
            sqlQuery.append("WHERE f.film_id IN (\n");
            sqlQuery.append(searchMap.containsKey("title")
                    ? "SELECT film_id\n" +
                    "FROM FILMS\n" +
                    "WHERE LOWER(film_name) LIKE :stringSearch\n"
                    + (searchMap.containsKey("director") ? "UNION\n" : "")
                    : "");
            sqlQuery.append(searchMap.containsKey("director")
                    ? "SELECT f.film_id\n" + "" +
                    "FROM FILMS f\n" +
                    "INNER JOIN director_films df ON f.film_id = df.film_id\n" +
                    "INNER JOIN directors d ON df.director_id = d.director_id\n" +
                    "WHERE LOWER(d.director_name) LIKE :stringSearch\n "
                    : "");
            sqlQuery.append(")\n");
        }
        sqlQuery.append("GROUP BY f.film_id\n" +
                "ORDER BY COUNT(lf.user_id) DESC, f.film_id\n" +
                ") search_film\n" +
                "LEFT JOIN films f ON f.film_id = search_film.film_id\n" +
                "LEFT JOIN genre_films gf ON f.film_id = gf.film_id\n" +
                "LEFT JOIN genre g ON gf.genre_id = g.genre_id\n" +
                "LEFT JOIN director_films df ON f.film_id = df.film_id\n" +
                "LEFT JOIN directors d ON df.director_id = d.director_id \n");
        String stringSearch = searchMap.values().stream().findAny().orElse("").toLowerCase();
        var param = Map.of("stringSearch", stringSearch.isBlank() ? "" : "%" + stringSearch + "%");
        var listSearch = jdbcTemplate.query(sqlQuery.toString(), param, filmExtractor);
        return new ArrayList<>(listSearch);
    }
}