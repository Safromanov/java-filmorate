package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
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

    private  Set<Long> friends;
    private  Set<Long> likedFilms;
    public User(String email, String login, String name, LocalDate birthday) {
        this.id =  -1;
        this.email = email;
        this.login = login;
        this.name = name;
        this.birthday = birthday;
        friends = new HashSet<>();
        likedFilms = new HashSet<>();
    }

}
