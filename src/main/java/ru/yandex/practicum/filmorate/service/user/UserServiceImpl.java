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

    public void friend(User user, User anotherUser) {
        if (anotherUser.equals(user)) throw new RuntimeException("Себя не зафрендить");
        friendsStorage.friend(user.getId(), anotherUser.getId());
    }

    public void unfriend(User user, User anotherUser) {
        if (!user.getFriends().containsKey(anotherUser.getId()))
            throw new RuntimeException("Пользователи не являются друзьями");
        user.getFriends().remove(anotherUser.getId());
        anotherUser.getFriends().remove(user.getId());
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
        return userStorage.getCommonFriends(userId, friendId);
    }

}
