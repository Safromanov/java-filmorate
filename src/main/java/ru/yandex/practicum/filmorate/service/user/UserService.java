package ru.yandex.practicum.filmorate.service.user;

import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.List;
import java.util.Optional;

public interface UserService extends UserStorage {

    Optional<User> getUser(long id);

    List<User> getFriends(long id);

    List<User> getCommonFriends(long userId, long friendId);

    void friend(User user, User anotherUser);

    void unfriend(User user, User anotherUser);

}
