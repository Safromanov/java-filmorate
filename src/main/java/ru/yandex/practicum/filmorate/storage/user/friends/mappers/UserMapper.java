package ru.yandex.practicum.filmorate.storage.user.friends.mappers;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class UserMapper implements RowMapper<User> {
    @Override
    public User mapRow(ResultSet resultSet, int i) throws SQLException {
        var id = Long.parseLong(resultSet.getString("user_id"));
        var email = resultSet.getString("email");
        var login = resultSet.getString("login");
        var name = resultSet.getString("user_name");
        if (name.isBlank()) name = login;
        var birthday = resultSet.getDate("birthday").toLocalDate();
        return User.builder()
                .id(id)
                .email(email)
                .login(login)
                .name(name)
                .birthday(birthday)
                .build();
    }
}