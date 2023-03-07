package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.List;

public interface UserStorage {
    Collection<User> findAll();
    User getUser(long id);
    User create(User user);
    User update(User user);
    List<User> getFriends(long id);
    List<User> getCommonFriends(long id1, long id2);

}
