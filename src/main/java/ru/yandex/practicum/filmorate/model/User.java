package ru.yandex.practicum.filmorate.model;


import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import java.time.LocalDate;

@Data
public class User {

    private int id;
    @Email
    private String email;
    @NotBlank
    private String login;

    private String name;
    @Past
    @NotNull
    private LocalDate birthday;

    public User(String email, String login, String name, LocalDate birthday) {
        this.id = -1;
        this.email = email;
        this.login = login;
        this.name = name;
        this.birthday = birthday;
    }

}
