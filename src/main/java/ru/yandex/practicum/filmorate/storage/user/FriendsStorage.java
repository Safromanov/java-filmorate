package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface FriendsStorage {
    void friend(long id1, long id2);
    List<User> getFriends(long id);
    List<User> getCommonFriends(long userId, long friendId);
    void friend(User user, User anotherUser);
    void unfriend(User user, User anotherUser);
}
