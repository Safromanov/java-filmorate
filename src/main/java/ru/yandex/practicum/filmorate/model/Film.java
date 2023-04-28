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
import java.time.Duration;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;


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
    private Set<Director> directors;
    private Set<Genre> genres;
    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.NUMBER_INT)
    @FilmDuration
    private Duration duration;
    @JsonIgnore
    private List<Long> idUsersWhoLike;

    public void addGenre(Genre genre) {
        genres.add(genre);
    }

    public void addDirector(Director director) {
        directors.add(director);
    }

}

