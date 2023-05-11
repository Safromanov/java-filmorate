package ru.yandex.practicum.filmorate.storage.film.mappers;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Director;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class DirectorMapper implements RowMapper<Director> {
    @Override
    public Director mapRow(ResultSet resultSet, int i) throws SQLException {
        if (resultSet.getString("director_id") == null) return null;
        long id = Long.parseLong(resultSet.getString("director_id"));
        String name = resultSet.getString("director_name");
        return Director.builder()
                .id(id)
                .name(name)
                .build();
    }
}
