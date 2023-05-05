package ru.yandex.practicum.filmorate.service.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.*;
import ru.yandex.practicum.filmorate.storage.event.EventStorage;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;
import ru.yandex.practicum.filmorate.storage.user.friends.FriendsStorage;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserStorage userStorage;
    private final FilmStorage filmStorage;
    private final FriendsStorage friendsStorage;
    private final EventStorage eventStorage;

    @Override
    public void friend(long id1, long id2) {
        friendsStorage.friend(id1, id2);
        eventStorage.addToEventFeed(id1, id2, EventType.FRIEND, OperationType.ADD);
    }

    @Override
    public void unfriend(long id1, long id2) {
        friendsStorage.unfriend(id1, id2);
        eventStorage.addToEventFeed(id1, id2, EventType.FRIEND, OperationType.REMOVE);
    }

    public List<Film> getRecommendedFilms(long id) {
        return filmStorage.createListFilmsById(userStorage.getRecommendedFilmsId(id));
    }

    @Override
    public void deleteUser(long userId) {
        userStorage.deleteUser(userId);
    }

    @Override
    public List<User> findAll() {
        return userStorage.findAll();
    }

    @Override
    public User add(User user) {
        if (user.getName().isBlank())
            user.setName(user.getLogin());
        return userStorage.add(user);
    }

    @Override
    public User update(User user) {
        return userStorage.update(user);
    }

    @Override
    public Optional<User> getUser(long id) {
        return userStorage.getUser(id);
    }

    @Override
    public List<User> getFriends(long id) {
        return userStorage.getFriends(id);
    }

    @Override
    public List<User> getCommonFriends(long userId, long friendId) {
        return userStorage.getCommonFriends(userId, friendId);
    }

    public List<Event> getEventFeed(long userId) {
        return eventStorage.getEventFeed(userId);
    }
}