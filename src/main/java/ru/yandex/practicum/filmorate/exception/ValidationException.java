package ru.yandex.practicum.filmorate.exception;

public class ValidationException extends IllegalArgumentException {

    public ValidationException(String msg) {
        super(msg);
    }

}
