package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.sql.ResultSet;
import java.sql.SQLException;

@Data
@AllArgsConstructor
@Builder
public class Genre {
    private final int id;
    private String name;

    public static Genre makeGenre(ResultSet resultSet) throws SQLException {
        var id = Integer.parseInt(resultSet.getString("genre_id"));
        var name = resultSet.getString("genre_name");
        return builder()
                .id(id)
                .name(name)
                .build();

    }
}
