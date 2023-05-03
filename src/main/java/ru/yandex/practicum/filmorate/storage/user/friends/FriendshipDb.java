package ru.yandex.practicum.filmorate.storage.user.friends;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.ValidationException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Primary
@Repository
@RequiredArgsConstructor
public class FriendshipDb implements FriendsStorage {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    @Override
    public void friend(long id1, long id2) {
        try {
            var friendsIdUserOne = getListFriendsId(id1);
            var friendsIdUserTwo = getListFriendsId(id2);

            if (isNotEquals(id1, id2)) {
                if (friendsIdUserOne.contains(id2)) {
                    throw new IllegalArgumentException("Пользователь уже добавлен в друзья");
                }

                if (friendsIdUserTwo.contains(id1)) {
                    String sqlDel = "DELETE FROM FRIENDSHIP WHERE USER_ID = :friend_id AND FRIEND_ID = :user_id";
                    var paramsDel = createParam(id1, id2); //!!!
                    jdbcTemplate.update(sqlDel, paramsDel);

                    String sqlAddFirst = "INSERT INTO FRIENDSHIP (USER_ID,FRIEND_ID,IS_CONFIRM)" +
                            "VALUES (:user_id,:friend_id,TRUE),\n" +
                            "(:friend_id,:user_id,TRUE);";
                    var paramsAdd = createParam(id1, id2);
                    jdbcTemplate.update(sqlAddFirst, paramsAdd);
                } else {
                    String sqlAddFirst = "INSERT INTO FRIENDSHIP (USER_ID,FRIEND_ID,IS_CONFIRM)" +
                            "VALUES (:user_id,:friend_id,FALSE)";
                    var paramsAdd = createParam(id1, id2);

                    jdbcTemplate.update(sqlAddFirst, paramsAdd);

                }
            }
        } catch (RuntimeException e) {
            throw new ValidationException("Неверный id");
        }
    }

    private List<Long> getListFriendsId(long id) {
        String sqlFriends = "SELECT USER_ID " +
                "\tFROM FRIENDSHIP \n" +
                "\tWHERE USER_ID = :user_id";

        Map<String, Object> paramsFriends = new HashMap<>();
        paramsFriends.put("user_id", id);

        return jdbcTemplate.queryForList(sqlFriends, paramsFriends, Long.class);
    }


    @Override
    public void unfriend(long id1, long id2) {
        String sql = "DELETE FROM friendship WHERE user_id = :user_id and friend_id = :friend_id;";
        var params = createParam(id1, id2);

        jdbcTemplate.update(sql, params);
    }

    private Map<String, Long> createParam(long id1, long id2) {
        Map<String, Long> params = new HashMap<>();
        params.put("user_id", id1);
        params.put("friend_id", id2);
        return params;
    }

    private boolean isNotEquals(Long id1, Long id2) {
        if (id1.equals(id2)) {
            throw new ValidationException("Пользователь не может совершать действие с самим собой (id № " + id1 + " )");
        }
        return true;
    }
}