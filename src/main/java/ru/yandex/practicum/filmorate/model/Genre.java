package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.ResultSet;
import java.sql.SQLException;

@Data
@AllArgsConstructor
@Builder
@Entity
@Table
public class Genre {
    @Id
    private final int id;

    private String name;

    public static Genre makeGenre(ResultSet resultSet) throws SQLException {
        if (resultSet.getString("genre_id") == null) return null;
        var id = Integer.parseInt(resultSet.getString("genre_id"));
        var name = resultSet.getString("genre_name");
        return builder()
                .id(id)
                .name(name)
                .build();

    }
}
