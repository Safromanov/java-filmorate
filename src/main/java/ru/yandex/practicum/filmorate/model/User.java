package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.HashSet;
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
    @JsonSerialize
    private Map<Long, Boolean> friends;
    private Set<Long> likedFilms;
//    public User(String email, String login, String name, LocalDate birthday) {
//        this.id =  -1;
//        this.email = email;
//        this.login = login;
//        this.name = name;
//        this.birthday = birthday;
//        friends = new HashMap<>();
//        likedFilms = new HashSet<>();
//    }

    public static User makeUser(ResultSet resultSet) throws SQLException {
        var id = Long.parseLong(resultSet.getString("user_id"));
        var email = resultSet.getString("email");
        var login = resultSet.getString("login");
        var name = resultSet.getString("user_name");
        if (name.isBlank()) name = login;
        var birthday = resultSet.getDate("birthday").toLocalDate();
        return builder()
                .id(id)
                .email(email)
                .login(login)
                .name(name)
                .birthday(birthday)
                .build();
    }

}
