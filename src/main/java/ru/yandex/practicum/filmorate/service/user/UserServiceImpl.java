package ru.yandex.practicum.filmorate.service.user;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.Collection;
import java.util.List;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    private UserStorage userStorage;

    public void friend(User user, User anotherUser) {
        if (anotherUser.equals(user)) throw new RuntimeException("Себя не зафрендить");
        user.getFriends().add(anotherUser.getId());
        anotherUser.getFriends().add(user.getId());
    }

    public void unfriend(User user, User anotherUser){
        if (!user.getFriends().contains(anotherUser.getId()))
            throw new RuntimeException("Пользователи не являются друзьями");
        user.getFriends().remove(anotherUser.getId());
        anotherUser.getFriends().remove(user.getId());
    }

    @Override
    public Collection<User> findAll() {
        return userStorage.findAll();
    }

    @Override
    public User create(User user) {
        return userStorage.create(user);
    }

    @Override
    public User update(User user) {
        return userStorage.update(user);
    }

    @Override
    public User getUser(long id) {
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
}
