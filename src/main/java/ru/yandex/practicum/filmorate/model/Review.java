package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Builder
public class Review {
    @JsonProperty("reviewId")
    private Long id;
    @NotBlank(message = "Отзыв не может быть пустым")
    private String content;
    @NotNull
    private Boolean isPositive;
    @NotNull(message = "id пользователя должен быть задан")
    private Long userId;
    @NotNull(message = "id фильма должен быть задан")
    private Long filmId;
    private Integer useful; //кол-во лайков обзора
}