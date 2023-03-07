package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.web.bind.annotation.*;

import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import javax.validation.Valid;
import java.util.*;

@Slf4j
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserStorage userStorage;
    private final UserService userService;


    @GetMapping
    public Collection<User> findAll() {
        return userStorage.findAll();
    }

    @PostMapping
    public User create(@Valid @RequestBody User user) {
        return userStorage.create(user);
    }

    @PutMapping
    public User update(@Valid @RequestBody User user) {
        return userStorage.update(user);
    }

    @GetMapping("{id}")
    public User getUser(@PathVariable long id) {
        return userStorage.getUser(id);
    }

    @GetMapping("{id}/friends")
    public List<User> getFriends(@PathVariable long id) {
        return userStorage.getFriends(id);
    }

    @GetMapping("{id}/friends/common/{otherId}")
    public List<User> getCommonFriends(@PathVariable long id, @PathVariable long otherId) {
        return userStorage.getCommonFriends(id, otherId);
    }

    @PutMapping("{id}/friends/{friendId}")
    public void friend(@PathVariable long id, @PathVariable long friendId){
        userService.friend(userStorage.getUser(id), userStorage.getUser(friendId));
    }

    @DeleteMapping ("{id}/friends/{friendId}")
    public void unfriend(@PathVariable long id, @PathVariable long friendId){
        userService.unfriend(userStorage.getUser(id), userStorage.getUser(friendId));
    }

}
