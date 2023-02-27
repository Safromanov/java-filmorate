package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

import ru.yandex.practicum.filmorate.validator.FilmDuration;
import ru.yandex.practicum.filmorate.validator.FilmReleaseDate;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.Duration;
import java.time.LocalDate;


@Data
public class Film {
    int id;
    @NotBlank
    String name;

    @Size(max = 200)
    String description;
    @FilmReleaseDate
    LocalDate releaseDate;
    @JsonFormat(shape = JsonFormat.Shape.NUMBER_INT)
    @FilmDuration
    Duration duration;

    public Film (String name, String description, LocalDate releaseDate, Duration duration) {
        this.id = -1;
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
    }
}

