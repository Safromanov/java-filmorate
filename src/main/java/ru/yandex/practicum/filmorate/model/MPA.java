package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@RequiredArgsConstructor
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum MPA {
    G(1, "G"),
    PG(2, "PG"),
    PG13(3, "PG-13"),
    R(4, "R"),
    NC17(5, "PG-17");

    @Getter
    final int id;
    @Getter
    final private String name;

    @JsonCreator
    public static MPA findValue(@JsonProperty("id") int id){
        return Arrays.stream(MPA.values()).filter(pt -> pt.id == id).findFirst().get();
    }

}



