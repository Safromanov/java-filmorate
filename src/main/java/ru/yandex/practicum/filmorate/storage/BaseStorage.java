package ru.yandex.practicum.filmorate.storage;

import java.util.List;

public interface BaseStorage<T> {

    List<T> findAll();

    T add(T t);

    T update(T t);
}