package ru.yandex.practicum.filmorate.storage.film.mappers;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.MPA;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Duration;
import java.time.LocalDate;
import java.util.HashSet;

@Component
public class FilmMapper implements RowMapper<Film> {
    @Override
    public Film mapRow(ResultSet resultSet, int i) throws SQLException {
        long id = Long.parseLong(resultSet.getString("film_id"));
        String filmName = resultSet.getString("film_name");
        String description = resultSet.getString("description");
        long duration = Long.parseLong(resultSet.getString("duration_minute"));
        int mpaId = Integer.parseInt(resultSet.getString("mpa_id"));
        LocalDate releaseDate = resultSet.getDate("release_date").toLocalDate();
        return Film.builder()
                .id(id)
                .name(filmName)
                .description(description)
                .duration(Duration.ofSeconds(duration))
                .mpa(MPA.findValue(mpaId))
                .releaseDate(releaseDate)
                .genres(new HashSet<>())
                .directors(new HashSet<>())
                .build();
    }
}