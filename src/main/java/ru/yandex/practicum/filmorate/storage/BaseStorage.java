package ru.yandex.practicum.filmorate.storage;

import java.util.Collection;

public interface BaseStorage<T>{
    Collection<T> findAll();
    T create(T t);
    T update(T t);
}
