package ru.yandex.practicum.filmorate.storage.user.friends;


public interface FriendsStorage {

    void friend(long id1, long id2);

    void unfriend(long id1, long id2);

}
