package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.BaseStorage;

import java.util.List;
import java.util.Optional;

public interface UserStorage extends BaseStorage<User> {

    Optional<User> getUser(long id);

    List<User> getFriends(long id);

    List<User> getCommonFriends(long userId, long friendId);

    void deleteUser(long userId);
}
