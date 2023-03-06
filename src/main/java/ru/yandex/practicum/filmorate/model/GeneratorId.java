package ru.yandex.practicum.filmorate.model;


import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public class GeneratorId {
    private int id = 0;

    public int getId() {
       return ++id;
    }

}
