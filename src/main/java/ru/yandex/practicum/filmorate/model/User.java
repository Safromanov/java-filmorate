package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.Map;
import java.util.Set;

@Data
@Builder
public class User {

    private long id;
    @Email
    @NotEmpty
    private String email;
    @NotBlank
    @Pattern(regexp = "\\S+")
    private String login;
    private String name;
    @PastOrPresent
    @NotNull
    private LocalDate birthday;
    private Map<Long, Boolean> friends;
    @JsonIgnore
    private Set<Long> likedFilms;
}