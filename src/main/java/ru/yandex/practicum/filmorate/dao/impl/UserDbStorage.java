package ru.yandex.practicum.filmorate.dao.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;

@Component("UserDbStorage")
@Primary
@RequiredArgsConstructor
public class UserDbStorage implements UserStorage {
    private final JdbcTemplate jdbcTemplate;
    @Override
    public List<User> allUsers() {
        String sqlQuery = "SELECT * FROM users";
        return jdbcTemplate.query(sqlQuery, (rs, rowNum) -> makeUser(rs));
    }

    @Override
    public Optional<User> getUserById(int id) {
        String sql = "SELECT * FROM users WHERE id = ?";
        SqlRowSet userRows = jdbcTemplate.queryForRowSet(sql, id);

        if(userRows.next()) {
            User user = new User();
            user.setFriends(getUserFriendsById(userRows.getInt("id")));
            user.setId(userRows.getInt("id"));
            user.setEmail(userRows.getString("email"));
            user.setLogin(userRows.getString("login"));
            user.setName(userRows.getString("name"));
            user.setBirthday(Objects.requireNonNull(userRows.getDate("birthday")).toLocalDate());
            return Optional.of(user);
        } else {
            return Optional.empty();
        }
    }

    @Override
    public User add(User user) {
        String sqlQuery = "INSERT INTO users (email, login, name, birthday) VALUES (?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement statement = connection.prepareStatement(sqlQuery, new String[]{"id"});
            statement.setString(1, user.getEmail());
            statement.setString(2, user.getLogin());
            if(user.getName().isEmpty() || user.getName().isBlank()) {
                statement.setString(3, user.getLogin());
            } else {
                statement.setString(3, user.getName());
            }
            statement.setDate(4, Date.valueOf(user.getBirthday()));
            return statement;
        }, keyHolder);

        return getUserById(Objects.requireNonNull(keyHolder.getKey()).intValue()).orElse(null);
    }

    @Override
    public User update(User user) {
        String sqlQuery = "UPDATE users SET email = ?, login = ?, name = ?, birthday = ? WHERE id = ?";
        jdbcTemplate.update(sqlQuery, user.getEmail(), user.getLogin(), user.getName(),
                            user.getBirthday(), user.getId());
        if (getUserById(user.getId()).isEmpty()) {
            throw new NotFoundException(String.format("Пользователя с ID: %d нет в БД!", user.getId()));
        }else {
            return getUserById(user.getId()).orElse(null);
        }
    }

    @Override
    public void delete(User user) {
        String sqlQuery = "DELETE FROM users WHERE id = ?";
        jdbcTemplate.update(sqlQuery, user.getId());
    }

    public Set<Long> getUserFriendsById(Integer userId) {
        String sqlQuery = "SELECT * FROM user_friends WHERE id = ?";
        return new HashSet<>(jdbcTemplate.query(sqlQuery, (rs, rowNum) -> rs.getLong("friends_with"), userId));
    }

    private User makeUser(ResultSet rs) throws SQLException {
        User user = new User();
        user.setFriends(getUserFriendsById(rs.getInt("id")));
        user.setId(rs.getInt("id"));
        user.setEmail(rs.getString("email"));
        user.setLogin(rs.getString("login"));
        user.setName(rs.getString("name"));
        user.setBirthday(rs.getDate("birthday").toLocalDate());
        return user;
    }
}