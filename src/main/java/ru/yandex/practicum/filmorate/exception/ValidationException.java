package ru.yandex.practicum.filmorate.exception;

import java.util.NoSuchElementException;

public class ValidationException extends IllegalArgumentException{

    public ValidationException(String msg) {
        super(msg);
    }



}
