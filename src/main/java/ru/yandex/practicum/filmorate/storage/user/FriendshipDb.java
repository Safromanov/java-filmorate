package ru.yandex.practicum.filmorate.storage.user;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.PreparedStatement;
import java.util.List;

import static ru.yandex.practicum.filmorate.model.User.makeUser;

@Primary
@Repository
@RequiredArgsConstructor
public class FriendshipDb implements FriendsStorage {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public void friend(long id1, long id2) {
        Integer[] friendshipStatus = getFriendshipStatus(id1, id2);
        if (friendshipStatus[0] == 1)
            throw new ValidationException("Пользователь уже добавлен в друзья");
        String sql = "INSERT INTO friendship (USER_ID,  FRIEND_ID) VALUES (?,?);";
        PreparedStatementCreator preparedStatementCreator = con -> {
            PreparedStatement stmt = con.prepareStatement(sql, new String[]{"user_id"});
            stmt.setString(1, String.valueOf(id1));
            stmt.setString(2, String.valueOf(id2));
            return stmt;
        };
        try {
            jdbcTemplate.update(preparedStatementCreator);
        } catch (RuntimeException e) {
            throw new ValidationException("Неверный id");
        }
    }

    @Override
    public void unfriend(long id1, long id2) {
        Integer[] friendshipStatus = getFriendshipStatus(id1, id2);
        String sql = "DELETE FROM friendship WHERE user_id = ? and friend_id = ?;";
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
        String sql = "\n SELECT U.USER_ID, U.EMAIL, U.LOGIN, U.USER_NAME, U.BIRTHDAY \n" +
                "FROM FRIENDSHIP F \n" +
                "LEFT JOIN USERS U ON F.FRIEND_ID = U.USER_ID \n" +
                "WHERE f.USER_ID = ?; \n";
        PreparedStatementCreator preparedStatementCreator = con -> {
            PreparedStatement stmt = con.prepareStatement(sql, new String[]{"user_id"});
            stmt.setString(1, String.valueOf(id));
            return stmt;
        };
        RowMapper<User> rowMapper = (resultSet, rowNum) -> makeUser(resultSet);
        return jdbcTemplate.query(preparedStatementCreator, rowMapper);
    }

    @Override
    public List<User> getCommonFriends(long userId, long friendId) {
        String sqlGetCommonFriends = " \n" +
                "SELECT U.USER_ID, U.EMAIL, U.LOGIN, U.USER_NAME, U.BIRTHDAY \n" +
                "FROM (\n" +
                "SELECT b.friend_id  FROM ( \n" +
                "\tSELECT f.FRIEND_ID\n" +
                "\tFROM FRIENDSHIP F\n" +
                "\tWHERE f.USER_ID = ?) a\n" +
                "\tINNER JOIN ( \n" +
                "\tSELECT f.FRIEND_ID\n" +
                "\tFROM FRIENDSHIP F\n" +
                "\tWHERE f.USER_ID = ? ) b ON a.friend_id = b.friend_id\t\n" +
                ")  common \n" +
                "LEFT JOIN USERS U ON common.FRIEND_ID = U.USER_ID;";
        PreparedStatementCreator preparedStatementCreator = con -> {
            PreparedStatement stmt = con.prepareStatement(sqlGetCommonFriends, new String[]{"user_id"});
            stmt.setString(1, String.valueOf(userId));
            stmt.setString(2, String.valueOf(friendId));
            return stmt;
        };
        RowMapper<User> rowMapper = (resultSet, rowNum) -> makeUser(resultSet);
        return jdbcTemplate.query(preparedStatementCreator, rowMapper);

    }


    /* friendStatus[0] = rs.getInt(1); - 0 - не добавлял, 1 - добавил в друзья, 2 - в колонке задвоение, что то идёт не так
       friendStatus[1] = rs.getInt(2); 1 1 - друзья 0 1 второй добавил первого  и наоборот для 1 0 */
    private Integer[] getFriendshipStatus(long id1, long id2) {
        String sql = "select count(case when user_id = ? and friend_id = ? then 1 else null end), " +
                "count(case when user_id = ? and friend_id = ? then 1 else null end) from friendship;";
        PreparedStatementCreator preparedStatementCreator = con -> {
            PreparedStatement stmt = con.prepareStatement(sql, new String[]{"user_id"});
            stmt.setString(1, String.valueOf(id1));
            stmt.setString(2, String.valueOf(id2));
            stmt.setString(3, String.valueOf(id2));
            stmt.setString(4, String.valueOf(id1));
            return stmt;
        };
        RowMapper<Integer[]> rowMapper = (rs, rowNum) -> new Integer[]{rs.getInt(1), rs.getInt(2)};

        return jdbcTemplate.query(preparedStatementCreator, rowMapper).get(0);
    }

}
