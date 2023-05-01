package ru.yandex.practicum.filmorate.service.user;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface UserService {

    Collection<User> findAll();

    User add(User user);

    User update(User user);

    Optional<User> getUser(long id);

    List<User> getFriends(long id);

    List<User> getCommonFriends(long userId, long friendId);

    void friend(long id1, long id2);

    void unfriend(long id1, long id2);

    Collection<Film> getFilmRecommendations(long id);

    void deleteUser(long userId);
}