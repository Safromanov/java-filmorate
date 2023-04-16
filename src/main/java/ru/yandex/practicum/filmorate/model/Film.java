package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonFormat;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Data;

import ru.yandex.practicum.filmorate.validator.FilmDuration;
import ru.yandex.practicum.filmorate.validator.FilmReleaseDate;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Duration;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


@Data
@Builder
public class Film {

    private long id;
    @NotBlank
    private String name;
    @NotNull
    @Size(max = 200)
    private String description;
    @NotNull
    @FilmReleaseDate
    private LocalDate releaseDate;

    private MPA mpa;

    private List<Genre> genres;
    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.NUMBER_INT)
    @FilmDuration
    private Duration duration;
    @JsonIgnore
    private List<Long> idUsersWhoLike;

    public static Film makeFilm(ResultSet resultSet) throws SQLException {
        var id = Long.parseLong(resultSet.getString("film_id"));
        var film_name = resultSet.getString("film_name");
        var description = resultSet.getString("description");
        var duration = Long.parseLong(resultSet.getString("DURATION_MINUTE"));
        var mpaId = Integer.parseInt(resultSet.getString("mpa_id"));
        var releaseDate = resultSet.getDate("release_date").toLocalDate();
        return builder()
                .id(id)
                .name(film_name)
                .description(description)
                .duration(Duration.ofSeconds(duration))
                .mpa(MPA.findValue(mpaId))
                .releaseDate(releaseDate)
                .build();
    }

    public static Film makeFilm(ResultSet resultSet, Collection<Genre> genres) throws SQLException {
        var id = Long.parseLong(resultSet.getString("film_id"));
        var filmName = resultSet.getString("film_name");
        var description = resultSet.getString("description");
        var duration = Long.parseLong(resultSet.getString("DURATION_MINUTE"));
        var mpaId = Integer.parseInt(resultSet.getString("mpa_id"));
        var releaseDate = resultSet.getDate("release_date").toLocalDate();
        return builder()
                .id(id)
                .name(filmName)
                .description(description)
                .genres(new ArrayList<>(genres))
                .duration(Duration.ofSeconds(duration))
                .mpa(MPA.findValue(mpaId))
                .releaseDate(releaseDate)
                .build();
    }

}

