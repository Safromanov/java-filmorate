package ru.yandex.practicum.filmorate.storage.user;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.GeneratorId;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;

@Slf4j
@Component
@AllArgsConstructor
public class InMemoryUserStorage implements UserStorage {
    private final Map<Long, User> users;
    private final GeneratorId generatorId;


    public Collection<User> findAll() {
        log.debug("Текущее количество пользователей: {}", users.size());
        return users.values();
    }

    @Override
    public  Optional<User> getUser(long id) {
        var user = Optional.of(users.get(id));
      //  if (user == null) throw new ValidationException("Введён не существующий id");
        return user;
    }

    @Override
    public List<User> getFriends(long id) {
        List<User> friends = new ArrayList<>();
        for (var friendId : users.get(id).getFriends().keySet())
            friends.add(users.get(friendId));
        return friends;
    }

    @Override
    public List<User> getCommonFriends(long userId, long friendId) {
        var friendsOfOne = getUser(userId).get().getFriends().keySet();
        var friendsAnother = getUser(friendId).get().getFriends().keySet();
        var commonFriends = new ArrayList<User>();
        for (var idFriendOfOne : friendsOfOne)
            if (friendsAnother.contains(idFriendOfOne))
                commonFriends.add(users.get(idFriendOfOne));
        return commonFriends;
    }

    @Override
    public User create(User user) {
        changeEmptyUserName(user);
        user.setId(generatorId.getId());
        log.debug("Новый пользователь: {}", user);
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public User update(User user) {
        var updatedUser = users.get(user.getId());
        if (updatedUser == null)
            throw new ValidationException("Пользователя не существует");
        changeEmptyUserName(user);
        users.put(user.getId(), user);
        log.debug("Пользователь обновлён: {}", user);
        return user;
    }

    private void changeEmptyUserName(User user) {
        if (user.getName() == null || user.getName().isBlank()) user.setName(user.getLogin());
    }

}
