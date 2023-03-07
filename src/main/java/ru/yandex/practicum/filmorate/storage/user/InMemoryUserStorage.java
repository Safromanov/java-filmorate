package ru.yandex.practicum.filmorate.storage.user;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.GeneratorId;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;

@Slf4j
@Component
@AllArgsConstructor
public class InMemoryUserStorage implements UserStorage{
    private final Map<Long, User> users;
    private final GeneratorId generatorId;

    @GetMapping
    public Collection<User> findAll() {
        log.debug("Текущее количество пользователей: {}", users.size());
        return users.values();
    }

    @Override
    public User getUser(long id) {
        User user = users.get(id);
        if (user == null) throw new ValidationException("Введён не существующий id");
        return user;
    }

    public List<User> getFriends(long id){
        List<User> friends = new ArrayList<>();
        for (var friendId: users.get(id).getFriends())
            friends.add(users.get(friendId));
        return friends;
    }

    @Override
    public List<User> getCommonFriends(long id1, long id2) {
        var friendsOfOne = getUser(id1).getFriends();
        var friendsAnother = getUser(id2).getFriends();
        var commonFriends = new ArrayList<User>();
        for (var idFriendOfOne : friendsOfOne)
            if (friendsAnother.contains(idFriendOfOne))
                commonFriends.add(users.get(idFriendOfOne));
        return commonFriends; 
    }

    @PostMapping
    public User create(User user) {
        changeEmptyUserName(user);
        user.setId(generatorId.getId());
        log.debug("Новый пользователь: {}", user);
        users.put(user.getId(), user);
        return user;
    }

    @PutMapping
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
