package ru.yandex.practicum.filmorate.dao.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Service("UserDbFriendsService")
@Slf4j
@RequiredArgsConstructor
@Primary
public class UserDbService implements UserService {
    private final JdbcTemplate jdbcTemplate;

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
