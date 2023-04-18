package ru.yandex.practicum.filmorate.storage.user;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static ru.yandex.practicum.filmorate.model.User.makeUser;

@Primary
@Repository
@AllArgsConstructor
public class UserDbStorage implements UserStorage {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public Collection<User> findAll() {
        String sql = "select * from Users";
        RowMapper<User> rowMapper = (resultSet, rowNum) -> makeUser(resultSet);

        PreparedStatementCreator preparedStatementCreator = con -> con.prepareStatement(sql, new String[]{"user_id"});
        return jdbcTemplate.query(preparedStatementCreator, rowMapper);
    }

    @Override
    public User addFilm(User user) {
        String sql = "INSERT INTO USERS (LOGIN,  USER_NAME, EMAIL, BIRTHDAY) VALUES (?,?,?,?);";

        KeyHolder keyHolder = new GeneratedKeyHolder();

        PreparedStatementCreator preparedStatementCreator = con -> makeStatement(con, user, sql);

        jdbcTemplate.update(preparedStatementCreator, keyHolder);
        user.setId(keyHolder.getKey().longValue());
        if (user.getName().isBlank()) user.setName(user.getLogin());
        return user;
    }

    @Override
    public User update(User user) {
        String sql = "UPDATE USERS SET LOGIN=?, USER_NAME=?, EMAIL=?, BIRTHDAY=? WHERE USER_ID=?;";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        PreparedStatementCreator preparedStatementCreator = con -> {
            var stmt = makeStatement(con, user, sql);
            stmt.setString(5, String.valueOf(user.getId()));
            return stmt;
        };
        if (jdbcTemplate.update(preparedStatementCreator, keyHolder) == 0)
            throw new ValidationException("Пользователя не существует");
        return user;
    }

    @Override
    public Optional<User> getUser(long id) {
        String sql = "select * from Users where user_id = ?;";
        RowMapper<User> rowMapper = (resultSet, rowNum) -> makeUser(resultSet);

        PreparedStatementCreator preparedStatementCreator = con -> {
            PreparedStatement stmt = con.prepareStatement(sql, new String[]{"user_id"});
            stmt.setString(1, String.valueOf(id));
            return stmt;
        };

        try {
            return Optional.of(jdbcTemplate.query(preparedStatementCreator, rowMapper).get(0));
        } catch (Exception e) {
            throw new ValidationException(e.getLocalizedMessage());
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

    private PreparedStatement makeStatement(Connection con, User user, String sql) throws SQLException {
        PreparedStatement stmt = con.prepareStatement(sql, new String[]{"user_id"});
        stmt.setString(1, user.getLogin());
        stmt.setString(2, user.getName());
        stmt.setString(3, user.getEmail());
        stmt.setDate(4, Date.valueOf(user.getBirthday()));

        return stmt;
    }

}
