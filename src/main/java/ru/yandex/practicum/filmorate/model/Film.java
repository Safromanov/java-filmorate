package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonFormat;

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
   // private int mpaId;
    private List<Genre> genres;
    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.NUMBER_INT)
    @FilmDuration
    private Duration duration;

    private List<Long> idUsersWhoLike;


    //    public Film (String name, String description, LocalDate releaseDate, Duration duration) {
//        this.id = -1;
//        this.name = name;
//        this.description = description;
//        this.releaseDate = releaseDate;
//        this.duration = duration;
//        idUsersWhoLike = new ArrayList<>();
//    }
    public static Film makeFilm(ResultSet resultSet) throws SQLException {
        var id = Long.parseLong(resultSet.getString("film_id"));
        var film_name = resultSet.getString("film_name");
        var description = resultSet.getString("description");
     //   var genre = resultSet.getString("genre");

      //  var mpaId = (int[])resultSet.getArray("genre").getArray();
     //   System.out.println(mpaId.length);
        var releaseDate = resultSet.getDate("release_date").toLocalDate();
        return builder()
                .id(id)
                .name(film_name)
                .description(description)
            //    .genre(genre)
                //.mpaId(mpaId)
                .releaseDate(releaseDate)
                .build();
    }

}

