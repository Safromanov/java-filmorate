package ru.yandex.practicum.filmorate.service.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;
import ru.yandex.practicum.filmorate.storage.user.friends.FriendsStorage;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserStorage userStorage;

    private final FilmStorage filmStorage;

    private final FriendsStorage friendsStorage;

    public void friend(long id1, long id2) {
        friendsStorage.friend(id1, id2);
    }

    public void unfriend(long id1, long id2) {
        friendsStorage.unfriend(id1, id2);
    }

    public Collection<Film> getFilmRecommendations(long id) {
        return filmStorage.createCollectionFilmsById(userStorage.getFilmRecommendationsId(id));
    }

    public Collection<User> findAll() {
        return userStorage.findAll();
    }

    public User add(User user) {
        if (user.getName().isBlank())
            user.setName(user.getLogin());
        return userStorage.add(user);
    }

    public User update(User user) {
        return userStorage.update(user);
    }

    public Optional<User> getUser(long id) {
        return userStorage.getUser(id);
    }

    public List<User> getFriends(long id) {
        return friendsStorage.getFriends(id);
    }

    public List<User> getCommonFriends(long userId, long friendId) {
        return friendsStorage.getCommonFriends(userId, friendId);
    }
}
