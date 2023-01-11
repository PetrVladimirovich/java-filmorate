package ru.yandex.practicum.filmorate.dao.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserFriendsService;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Service("UserDbFriendsService")
@Slf4j
@RequiredArgsConstructor
@Primary
public class UserDbFriendsService implements UserFriendsService {
    private final JdbcTemplate jdbcTemplate;
    private final UserDbStorage userDbStorage;
    @Override
    public void addToFriends(Integer userId, Integer friendId){
        if(userDbStorage.getUserById(userId).isEmpty() || userDbStorage.getUserById(friendId).isEmpty()){
            throw new NotFoundException(String.format("Пользователя с ID: %d или c ID: %d не существует", userId, friendId));
        } else {
            String sqlQuery = "INSERT INTO user_friends (id, friends_with, confirmed_friend) VALUES (?, ?, NVL2 " +
                    "((SELECT * FROM user_friends WHERE confirmed_friend = TRUE AND id = ? AND friends_with = ?), TRUE, FALSE))";
            jdbcTemplate.update(sqlQuery, userId, friendId, friendId, userId);
        }
    }

    @Override
    public void deleteFromFriends(Integer userId, Integer friendId){
        String sqlQuery = "DELETE FROM user_friends WHERE id = ? AND friends_with = ?;";
        jdbcTemplate.update(sqlQuery, userId, friendId);
        jdbcTemplate.update(sqlQuery, friendId, userId);
    }

    @Override
    public Set<User> friendsOfUser(Integer userId) {
        String sqlQuery = "SELECT * FROM users WHERE id IN (SELECT friends_with " +
                "FROM user_friends WHERE id = ?) ORDER BY id";
        Set<User> users = new TreeSet<>(Comparator.comparing(User::getId));
        users.addAll(jdbcTemplate.query(sqlQuery, (rs, rowNum) -> makeUser(rs), userId));
        return users;
    }

    @Override
    public Set<User> commonFriends(Integer userId, Integer otherUserId) {
        String sqlQuery = "SELECT * FROM users WHERE id IN (SELECT friends_with FROM user_friends " +
                "WHERE id = ? AND friends_with IN (SELECT friends_with FROM user_friends " +
                "WHERE id = ?));";

        Set<User> users = new TreeSet<>(Comparator.comparing(User::getId));
        users.addAll(jdbcTemplate.query(sqlQuery, (rs, rowNum) -> makeUser(rs), userId, otherUserId));
        return users;
    }

    private Set<Long> getFriends(Integer userId) {
        String sqlQuery = "SELECT * FROM user_friends WHERE id = ?;";
        return new HashSet<>(jdbcTemplate.query(sqlQuery, (rs, rowNum) -> rs.getLong("friends_with"), userId));
    }

    private User makeUser(ResultSet rs) throws SQLException {
        User user = new User();
        user.setFriends(getFriends(rs.getInt("id")));
        user.setId(rs.getInt("id"));
        user.setEmail(rs.getString("email"));
        user.setLogin(rs.getString("login"));
        user.setName(rs.getString("name"));
        user.setBirthday(rs.getDate("birthday").toLocalDate());
        return user;
    }
}
