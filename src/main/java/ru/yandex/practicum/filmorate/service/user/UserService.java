package ru.yandex.practicum.filmorate.service.user;

import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

public interface UserService extends UserStorage {
    void friend(User user, User anotherUser);
    void unfriend(User user, User anotherUser);
}
