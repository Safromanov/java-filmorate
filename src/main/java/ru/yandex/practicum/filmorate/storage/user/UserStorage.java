package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.BaseStorage;

import java.util.List;

public interface UserStorage extends BaseStorage<User> {
    User getUser(long id);
    List<User> getFriends(long id);
    List<User> getCommonFriends(long userId, long friendId);
}
