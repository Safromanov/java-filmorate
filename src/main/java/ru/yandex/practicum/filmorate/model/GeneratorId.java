package ru.yandex.practicum.filmorate.model;

public class GeneratorId {
    private static int idFilms = 0;
    private static int idUsers = 0;

    public static int getIdFilms() {
       return ++idFilms;
    }

    public static int getIdUsers() {
        return ++idUsers;
    }


}
