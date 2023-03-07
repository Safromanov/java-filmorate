package ru.yandex.practicum.filmorate.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;


@Service
@AllArgsConstructor
public class UserService {

    public void friend(User user1, User user2) {
        if (user2.equals(user1)) throw new RuntimeException("Себя не зафрендить");
        user1.getFriends().add(user2.getId());
        user2.getFriends().add(user1.getId());
    }

    public void unfriend(User user1, User user2){
        if (!user1.getFriends().contains(user2.getId())) throw new RuntimeException("Пользователи не являются друзьями");
        user1.getFriends().remove(user2.getId());
        user2.getFriends().remove(user1.getId());
    }

}
