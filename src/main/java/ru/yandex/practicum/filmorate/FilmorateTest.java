package ru.yandex.practicum.filmorate;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.SqlReturnResultSet;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.MPA;

import java.sql.PreparedStatement;
import java.time.Duration;

import static ru.yandex.practicum.filmorate.model.Film.makeFilm;


public class FilmorateTest {

    public static void main(String[] args) {
        var a = Duration.ofMinutes(190);
        System.out.println(a.toMinutes());
        var b = Duration.ofMinutes(a.toMinutes());
        System.out.println(b);
    }

}
