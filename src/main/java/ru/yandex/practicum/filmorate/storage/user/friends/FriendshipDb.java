package ru.yandex.practicum.filmorate.storage.user.friends;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Primary
@Repository
@RequiredArgsConstructor
public class FriendshipDb implements FriendsStorage {

    private final RowMapper<User> userMapper;

    private final NamedParameterJdbcTemplate jdbcTemplate;

  /*  @Override
    public void friend(long id1, long id2) {
//        Integer[] friendshipStatus = getFriendshipStatus(id1, id2);
//        if (friendshipStatus[0] == 1)
//            throw new ValidationException("Пользователь уже добавлен в друзья");
        String sql = "INSERT INTO friendship (user_id,  friend_id) VALUES (:user_id, :friend_id);";
        var params = createParam(id1, id2);
        try {
            jdbcTemplate.update(sql, params);
        } catch (RuntimeException e) {
            throw new ValidationException("Неверный id");
        }
    }*/

    @Override
    public void friend(long id1, long id2) {
//        Integer[] friendshipStatus = getFriendshipStatus(id1, id2);
//        if (friendshipStatus[0] == 1)
//            throw new ValidationException("Пользователь уже добавлен в друзья");

        String sql = "INSERT INTO friendship (user_id,  friend_id) VALUES (:user_id, :friend_id);";
        var params = createParam(id1, id2);
        try {
            jdbcTemplate.update(sql, params);
        } catch (RuntimeException e) {
            throw new ValidationException("Неверный id");
        }
    }


    @Override
    public void unfriend(long id1, long id2) {
        String sql = "DELETE FROM friendship WHERE user_id = :user_id and friend_id = :friend_id;";
        var params = createParam(id1, id2);

        jdbcTemplate.update(sql, params);
    }

    @Override
    public List<User> getFriends(long id) {
        String sql = "\n SELECT U.USER_ID, U.EMAIL, U.LOGIN, U.USER_NAME, U.BIRTHDAY \n" +
                "FROM FRIENDSHIP F \n" +
                "LEFT JOIN USERS U ON F.FRIEND_ID = U.USER_ID \n" +
                "WHERE f.USER_ID = :user_id; \n";
        Map<String, Object> params = Collections.singletonMap("user_id", id);
        return jdbcTemplate.query(sql, params, userMapper);
    }

    @Override
    public List<User> getCommonFriends(long userId, long friendId) {
        String sqlGetCommonFriends = " \n" +
                "SELECT U.USER_ID, U.EMAIL, U.LOGIN, U.USER_NAME, U.BIRTHDAY \n" +
                "FROM (\n" +
                "SELECT b.friend_id  FROM ( \n" +
                "\tSELECT f.FRIEND_ID\n" +
                "\tFROM FRIENDSHIP F\n" +
                "\tWHERE f.USER_ID = :user_id) a\n" +
                "\tINNER JOIN ( \n" +
                "\tSELECT f.FRIEND_ID\n" +
                "\tFROM FRIENDSHIP F\n" +
                "\tWHERE f.USER_ID = :friend_id ) b ON a.friend_id = b.friend_id\t\n" +
                ")  common \n" +
                "LEFT JOIN USERS U ON common.FRIEND_ID = U.USER_ID;";
        var params = createParam(userId, friendId);
        return jdbcTemplate.query(sqlGetCommonFriends, params, userMapper);

    }

    private Map<String, Long> createParam(long id1, long id2) {
        Map<String, Long> params = new HashMap<>();
        params.put("user_id", id1);
        params.put("friend_id", id2);
        return params;
    }

}
