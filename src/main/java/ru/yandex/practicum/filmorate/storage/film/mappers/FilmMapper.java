package ru.yandex.practicum.filmorate.storage.film.mappers;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.MPA;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Duration;

@Component
public class FilmMapper implements RowMapper<Film> {
    @Override
    public Film mapRow(ResultSet resultSet, int i) throws SQLException {
        var id = Long.parseLong(resultSet.getString("film_id"));
        var filmName = resultSet.getString("film_name");
        var description = resultSet.getString("description");
        var duration = Long.parseLong(resultSet.getString("duration_minute"));
        var mpaId = Integer.parseInt(resultSet.getString("mpa_id"));
        var releaseDate = resultSet.getDate("release_date").toLocalDate();
        return Film.builder()
                .id(id)
                .name(filmName)
                .description(description)
                .duration(Duration.ofSeconds(duration))
                .mpa(MPA.findValue(mpaId))
                .releaseDate(releaseDate)
                .build();
    }

}