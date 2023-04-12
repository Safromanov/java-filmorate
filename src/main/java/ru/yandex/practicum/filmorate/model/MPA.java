package ru.yandex.practicum.filmorate.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum MPA {
    G("G"), PG ("PG"), PG13("PG-13"), R("R"), NC17("NC-17");
    @Getter
    final String mpa;

}
