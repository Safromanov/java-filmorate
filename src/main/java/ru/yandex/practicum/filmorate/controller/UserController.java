package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.web.bind.annotation.*;

import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.user.UserServiceImpl;
import ru.yandex.practicum.filmorate.storage.user.FriendsStorage;


import javax.validation.Valid;
import java.util.*;

@Slf4j
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserServiceImpl userService;

    private final FriendsStorage friendsStorage;


    @GetMapping
    public Collection<User> findAll() {
        return userService.findAll();
    }

    @PostMapping
    public User create(@Valid @RequestBody User user) {
        return userService.create(user);
    }

    @PutMapping
    public User update(@Valid @RequestBody User user) {
        return userService.update(user);
    }

    @GetMapping("{id}")
    public User getUser(@PathVariable long id) {
        return userService.getUser(id).get();
    }

    @GetMapping("{id}/friends")
    public List<User> getFriends(@PathVariable long id) {
        return friendsStorage.getFriends(id);
    }

    @GetMapping("{id}/friends/common/{otherId}")
    public List<User> getCommonFriends(@PathVariable long id, @PathVariable long otherId) {
        return friendsStorage.getCommonFriends(id, otherId);
    }

    @PutMapping("{id}/friends/{friendId}")
    public void friend(@PathVariable long id, @PathVariable long friendId) {
        friendsStorage.friend(id, friendId);
    }

    @DeleteMapping("{id}/friends/{friendId}")
    public void unfriend(@PathVariable long id, @PathVariable long friendId) {
        friendsStorage.unfriend(id, friendId);
    }

}
