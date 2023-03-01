package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.web.bind.annotation.*;

import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.GeneratorId;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.util.*;

@Slf4j
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final Map<Integer, User> users;
    private final GeneratorId generatorId;

        @GetMapping
    public Collection<User> findAll() {
        log.debug("Текущее количество пользователей: {}", users.size());
        return users.values();
    }

    @PostMapping
    public User create(@Valid @RequestBody User user) {
        changeEmptyUserName(user);
        user.setId(generatorId.getId());
        log.debug("Новый пользователь: {}", user);
        users.put(user.getId(), user);
        return user;
    }

    @PutMapping
    public User update(@Valid @RequestBody User user) {
        if (!users.containsKey(user.getId()))
            throw new ValidationException("Пользователя не существует");
        changeEmptyUserName(user);
        users.put(user.getId(), user);
        log.debug("Пользователь обновлён: {}", user);
        return user;
    }

    private void changeEmptyUserName(User user) {
        if (user.getName() == null || user.getName().isBlank()) user.setName(user.getLogin());
    }

}
