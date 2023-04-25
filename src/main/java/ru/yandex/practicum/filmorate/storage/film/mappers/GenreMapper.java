package ru.yandex.practicum.filmorate.storage.film.mappers;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;


@Component
public class GenreMapper implements RowMapper<Genre> {

    @Override
    public Genre mapRow(ResultSet resultSet, int i) throws SQLException {
        if (resultSet.getString("genre_id") == null) return null;
        var id = Integer.parseInt(resultSet.getString("genre_id"));
        var name = resultSet.getString("genre_name");
        return Genre.builder()
                .id(id)
                .name(name)
                .build();
    }

}
