package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.persistence.Id;
import java.util.Arrays;
import java.util.NoSuchElementException;


@RequiredArgsConstructor
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum MPA {

    G(1, "G"),
    PG(2, "PG"),
    PG13(3, "PG-13"),
    R(4, "R"),
    NC17(5, "NC-17");
    @Id
    @Getter
    final int id;
    @Getter
    final String name;

    @JsonCreator
    public static MPA findValue(@JsonProperty("id") int id) {
        try {
            return Arrays.stream(MPA.values()).filter(pt -> pt.id == id).findFirst().get();
        } catch (RuntimeException e) {
            throw new NoSuchElementException("Неверный возрастной рейтинг");
        }
    }
}