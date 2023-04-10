package ru.yandex.practicum.filmorate.storage.user;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.*;
import java.util.List;
import java.util.Map;

import static ru.yandex.practicum.filmorate.model.User.makeUser;
@Primary
@Component
@RequiredArgsConstructor
public class FriendshipDb implements FriendsStorage {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public void friend(long id1, long id2) {
        String sql = "INSERT INTO friendship (USER_ID,  FRIEND_ID) VALUES (?,?);";
        Integer[] friendshipStatus = getFriendshipStatus(id1, id2);
        if (friendshipStatus[0] == 1)
            throw new RuntimeException("Пользователь уже добавлен в друзья");
        PreparedStatementCreator preparedStatementCreator = con -> {
            PreparedStatement stmt = con.prepareStatement(sql, new String[]{"user_id"});
            stmt.setString(1, String.valueOf(id1));
            stmt.setString(2, String.valueOf(id2));
            return stmt;
        };
        jdbcTemplate.update(preparedStatementCreator);
    }

    @Override
    public List<User> getFriends(long id) {
        String sql = String.format(
                "SELECT u.user_id, u.email, u.login, u.user_name, u.birthday " +
                "FROM (SELECT e.friend_id "  +
                    "FROM   (select  * from friendship " +
                    "where USER_ID = ?) e " +
                "left join friendship f on e.FRIEND_ID = f.USER_ID " +
                "where f.FRIEND_ID = ?) p " +
                "left join users u on p.FRIEND_ID = u.USER_ID;");
        PreparedStatementCreator preparedStatementCreator = con -> {
            PreparedStatement stmt = con.prepareStatement(sql, new String[]{"user_id"});
            stmt.setString(1, String.valueOf(id));
            stmt.setString(2, String.valueOf(id));
            return stmt;
        };
        RowMapper<User> rowMapper = (resultSet, rowNum) -> makeUser(resultSet);
        return jdbcTemplate.query(preparedStatementCreator, rowMapper);
    }

    @Override
    public List<User> getCommonFriends(long userId, long friendId) {
        return null;
    }

    @Override
    public void friend(User user, User anotherUser) {

    }

    @Override
    public void unfriend(User user, User anotherUser) {

    }


    /* friendStatus[0] = rs.getInt(1); - 0 - не добавлял, 1 - добавил в друзья, 2 - в колонке задвоение, что то идёт не так
       friendStatus[1] = rs.getInt(2); 1 1 - друзья 0 1 второй добавил первого  и наоборот для 1 0 */
    private Integer[] getFriendshipStatus(long id1, long id2) {
        String sql = String.format("select count(case when user_id = ? and friend_id = ? then 1 else null end), " +
                "count(case when user_id = ? and friend_id = ? then 1 else null end) from friendship;");
        PreparedStatementCreator preparedStatementCreator = con -> {
            PreparedStatement stmt = con.prepareStatement(sql, new String[]{"user_id"});
            stmt.setString(1, String.valueOf(id1));
            stmt.setString(2, String.valueOf(id2));
            stmt.setString(3, String.valueOf(id2));
            stmt.setString(4, String.valueOf(id1));
            return stmt;
        };

        RowMapper<Integer[]> rowMapper = (rs, rowNum) -> new Integer[]{ rs.getInt(1),  rs.getInt(2)};


        // Map.Entry<Boolean, Boolean> ;
        return jdbcTemplate.query(preparedStatementCreator, rowMapper).get(0);
    }
}
