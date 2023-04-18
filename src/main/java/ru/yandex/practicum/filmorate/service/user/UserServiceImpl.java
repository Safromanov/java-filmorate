package ru.yandex.practicum.filmorate.service.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.FriendsStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserStorage userStorage;

    private final FriendsStorage friendsStorage;

    @Override
    public void friend(long id1, long id2) {
        friendsStorage.friend(id1, id2);
    }

    @Override
    public void unfriend(long id1, long id2) {
        friendsStorage.unfriend(id1, id2);
    }

    @Override
    public Collection<User> findAll() {
        return userStorage.findAll();
    }

    @Override
    public User addFilm(User user) {
        return userStorage.addFilm(user);
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
        return friendsStorage.getFriends(id);
    }

    @Override
    public List<User> getCommonFriends(long userId, long friendId) {
        return friendsStorage.getCommonFriends(userId, friendId);
    }

}
