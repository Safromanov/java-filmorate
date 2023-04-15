package ru.yandex.practicum.filmorate.controller;

import org.hibernate.ObjectNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.yandex.practicum.filmorate.exception.ValidationException;

import java.util.List;
import java.util.NoSuchElementException;

@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public List<String> handleValidationError(final ValidationException e) {
        return List.of(e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public List<String> handleValidationError(NoSuchElementException e) {
        return List.of(e.getMessage());}


    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public List<String> handleValidationError(ObjectNotFoundException e) {
        return List.of(e.getMessage());}
}
