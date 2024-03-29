package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.MPA;

import java.util.Arrays;
import java.util.Collection;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;


@Slf4j
@RestController
@RequestMapping("/mpa")
@RequiredArgsConstructor
public class MPAController {

    @GetMapping("{id}")
    public MPA getMPA(@PathVariable int id) {
        try {
            return MPA.findValue(id);
        } catch (RuntimeException e) {
            throw new NoSuchElementException("Неверный возрастной рейтинг");
        }
    }

    @GetMapping
    public Collection<MPA> getAllMPA() {
        return Arrays.stream(MPA.values()).collect(Collectors.toList());
    }
}