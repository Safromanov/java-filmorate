package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

import ru.yandex.practicum.filmorate.validator.FilmDuration;
import ru.yandex.practicum.filmorate.validator.FilmReleaseDate;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.Duration;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
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
    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.NUMBER_INT)
    @FilmDuration
    private Duration duration;

    private List<Long> idUsersWhoLike;

    public Film (String name, String description, LocalDate releaseDate, Duration duration) {
        this.id = -1;
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
        idUsersWhoLike = new ArrayList<>();
    }


}

