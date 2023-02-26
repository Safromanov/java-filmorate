package ru.yandex.practicum.filmorate.exception;

public class InvalidEmailException extends IllegalArgumentException{
    public InvalidEmailException() {
        super("Email не может быть пустой");
    }
}
