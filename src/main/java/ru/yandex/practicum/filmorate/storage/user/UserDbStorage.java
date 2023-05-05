package ru.yandex.practicum.filmorate.storage.user;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;

@Primary
@Repository
@AllArgsConstructor
public class UserDbStorage implements UserStorage {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    private final RowMapper<User> userMapper;

    @Override
    public List<User> findAll() {
        String sql = "select * from users";
        return jdbcTemplate.query(sql, userMapper);
    }

    @Override
    public User add(User user) {
        String sql = "INSERT INTO USERS (login,  user_name, email, birthday) " +
                "VALUES (:login, :user_name, :email, :birthday); ";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        Map<String, Object> params = new HashMap<>();
        params.put("login", user.getLogin());
        params.put("user_name", user.getName());
        params.put("email", user.getEmail());
        params.put("birthday", user.getBirthday());
        SqlParameterSource paramSource = new MapSqlParameterSource(params);
        jdbcTemplate.update(sql, paramSource, keyHolder);
        user.setId(keyHolder.getKey().longValue());
        return user;
    }

    @Override
    public List<User> getFriends(long id) {
        String sqlCheckUsr = "SELECT * FROM users WHERE user_id = :user_id";

        String sql = "\n SELECT U.USER_ID, U.EMAIL, U.LOGIN, U.USER_NAME, U.BIRTHDAY \n" +
                "FROM FRIENDSHIP F \n" +
                "LEFT JOIN USERS U ON F.FRIEND_ID = U.USER_ID \n" +
                "WHERE f.USER_ID = :user_id; \n";
        Map<String, Object> params = Collections.singletonMap("user_id", id);
        if (jdbcTemplate.query(sqlCheckUsr, params, userMapper).size() == 0)
            throw new ValidationException("Пользователя с таким id не существует");
        return jdbcTemplate.query(sql, params, userMapper);
    }

    @Override
    public User update(User user) {
        String sql = "UPDATE users SET login = :login, user_name = :user_name, email = :email, birthday = :birthday " +
                "WHERE user_id = :user_id";
        Map<String, Object> params = new HashMap<>();
        params.put("login", user.getLogin());
        params.put("user_name", user.getName());
        params.put("email", user.getEmail());
        params.put("birthday", user.getBirthday());
        params.put("user_id", user.getId());
        SqlParameterSource paramSource = new MapSqlParameterSource(params);
        if (jdbcTemplate.update(sql, paramSource) == 0)
            throw new ValidationException("Пользователя не существует");
        return user;
    }

    @Override
    public Optional<User> getUser(long id) {
        String sql = "select * from Users where user_id = :user_id;";
        String sqlGetIdFriends = "SELECT FRIEND_ID, IS_CONFIRM " +
                "FROM FRIENDSHIP \n " +
                "WHERE USER_ID = :user_id";
        Map<String, Object> param = Collections.singletonMap("user_id", id);
        try {
            User user = jdbcTemplate.queryForObject(sql, param, userMapper);
            HashMap<Long, Boolean> relationshipMap = new HashMap<>();
            SqlRowSet rsMap = jdbcTemplate.queryForRowSet(sqlGetIdFriends, param);
            while (rsMap.next()) {
                relationshipMap.put(rsMap.getLong("FRIEND_ID"), rsMap.getBoolean("IS_CONFIRM"));
            }
            user.setFriends(relationshipMap);
            return Optional.ofNullable(user);
        } catch (RuntimeException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<User> getCommonFriends(long userId, long friendId) {
        String sqlGetCommonFriends = " \n" +
                "SELECT U.USER_ID, U.EMAIL, U.LOGIN, U.USER_NAME, U.BIRTHDAY \n" +
                "FROM (\n" +
                "SELECT b.friend_id  FROM ( \n" +
                "   SELECT f.FRIEND_ID\n" +
                "   FROM FRIENDSHIP F\n" +
                "   WHERE f.USER_ID = :user_id) a \n" +
                "   INNER JOIN ( \n" +
                "   SELECT f.FRIEND_ID \n" +
                "   FROM FRIENDSHIP F \n" +
                "   WHERE f.USER_ID = :friend_id ) b ON a.friend_id = b.friend_id \n" +
                ")  common \n" +
                "LEFT JOIN USERS U ON common.FRIEND_ID = U.USER_ID;";
        Map<String, Long> params = Map.of("user_id", userId, "friend_id", friendId);
        return jdbcTemplate.query(sqlGetCommonFriends, params, userMapper);
    }

    @Override
    public List<Long> getRecommendedFilmsId(long id) {
        getUser(id).orElseThrow(() -> new ValidationException("Пользователя под таким id не существует"));
        Collection<User> users = findAll();
        if (findAll().isEmpty()) return null;
        Collection<Long> likedFilms = getLikedFilmsLikeId(id);
        HashMap<Long, Integer> sameLikes = new HashMap<>();
        for (User user : users)
            for (long filmId : getLikedFilmsLikeId(user.getId())) {
                if (likedFilms.contains(filmId) && user.getId() != id) {
                    sameLikes.put(user.getId(), 1 + Optional.ofNullable(sameLikes.get(user.getId())).orElse(0));
                }
            }
        int maxSameLikes = sameLikes.values().stream().max(Comparator.comparing(Integer::intValue)).orElse(0);
        Set<Long> filmsId = new TreeSet<>();
        for (Long otherId : sameLikes.keySet()) {
            if (sameLikes.get(otherId) == maxSameLikes) {
                for (long idFilm : getLikedFilmsLikeId(otherId)) {
                    if (!likedFilms.contains(idFilm))
                        filmsId.add(idFilm);
                }
            }
        }
        return new ArrayList<>(filmsId);
    }

    private Collection<Long> getLikedFilmsLikeId(long id) {
        String sqlQuery =
                "SELECT film_id " +
                        "FROM likes_film " +
                        "WHERE user_id = ? ";
        return jdbcTemplate.getJdbcTemplate().query(sqlQuery, (rs, rowNum) -> rs.getLong("film_id"), id);
    }

    @Override
    public void deleteUser(long userId) {
        getUser(userId).orElseThrow(() -> new ValidationException("Пользователя под таким id не существует"));
        jdbcTemplate.getJdbcTemplate().update("DELETE FROM users WHERE user_id = ?", userId);
    }
}