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
    public Collection<User> findAll() {
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
        String sqlGetFriends = "SELECT FRIEND_ID, IS_CONFIRM " +
                "FROM FRIENDSHIP \n " +
                "WHERE USER_ID = :user_id";
        Map<String, Object> params = Collections.singletonMap("user_id", id);
        try {
            User user = jdbcTemplate.queryForObject(sql, params, userMapper);
            HashMap<Long, Boolean> map = new HashMap<>();
            SqlRowSet rsMap = jdbcTemplate.queryForRowSet(sqlGetFriends, params);
            while (rsMap.next()) {
                map.put(rsMap.getLong("FRIEND_ID"), rsMap.getBoolean("IS_CONFIRM"));
            }
            user.setFriends(map);
            return Optional.ofNullable(user);
        } catch (RuntimeException e) {
            throw new ValidationException("getUser");
        }
    }

    @Override
    public List<User> getFriends(long id) {
        return null;
    }

    @Override
    public List<User> getCommonFriends(long userId, long friendId) {
        return null;
    }

    @Override
    public void deleteUser(long userId) {
        getUser(userId);
        jdbcTemplate.getJdbcTemplate().update("DELETE FROM likes_film WHERE user_id = ?", userId);
        jdbcTemplate.getJdbcTemplate().update("DELETE FROM friendship WHERE user_id = ? " +
                "OR friend_id = ?", userId, userId);
        jdbcTemplate.getJdbcTemplate().update("DELETE FROM likes_review WHERE user_id = ?", userId);
        jdbcTemplate.getJdbcTemplate().update("DELETE FROM reviews WHERE user_id = ?", userId);
        jdbcTemplate.getJdbcTemplate().update("DELETE FROM event_feed WHERE user_id = ?", userId);
        jdbcTemplate.getJdbcTemplate().update("DELETE FROM users WHERE user_id = ?", userId);
    }
}
