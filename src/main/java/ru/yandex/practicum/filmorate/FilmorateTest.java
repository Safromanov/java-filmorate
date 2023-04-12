package ru.yandex.practicum.filmorate;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import ru.yandex.practicum.filmorate.model.MPA;

@SpringBootApplication
public class FilmorateTest {

    public static void main(String[] args) {
        System.out.println(MPA.PG13.getMpa());
    }

}
