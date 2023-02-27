package ru.yandex.practicum.filmorate.model;


import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Past;
import java.time.LocalDate;

@Data
public class User {

    int id;
    @Email
    String email;
    @NotBlank
    String login;

    String name;
    @Past
    LocalDate birthday;

    public User(String email, String login, String name, LocalDate birthday) {
        this.id = -1;
        this.email = email;
        this.login = login;
        this.name = name;
        this.birthday = birthday;
    }


}
