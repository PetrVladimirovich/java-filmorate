package ru.yandex.practicum.filmorate.dao.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.FriendDao;
import ru.yandex.practicum.filmorate.exception.NotFoundException;

@Component("FriendDbStorage")
@RequiredArgsConstructor
public class FriendDbStorage implements FriendDao {
    private final JdbcTemplate jdbcTemplate;
    private final UserDbStorage userDbStorage;

    @Override
    public void addToFriends(Integer userId, Integer friendId) {
        if(userDbStorage.getUserById(userId).isEmpty() || userDbStorage.getUserById(friendId).isEmpty()){
            throw new NotFoundException(String.format("Пользователя с ID: %d или c ID: %d не существует", userId, friendId));
        } else {
            String sqlQuery = "INSERT INTO user_friends (id, friends_with, confirmed_friend) VALUES (?, ?, NVL2 " +
                    "((SELECT * FROM user_friends WHERE confirmed_friend = TRUE AND id = ? AND friends_with = ?), TRUE, FALSE))";
            jdbcTemplate.update(sqlQuery, userId, friendId, friendId, userId);
        }
    }

    @Override
    public void deleteFromFriends(Integer userId, Integer friendId) {
        String sqlQuery = "DELETE FROM user_friends WHERE id = ? AND friends_with = ?;";
        jdbcTemplate.update(sqlQuery, userId, friendId);
        jdbcTemplate.update(sqlQuery, friendId, userId);
    }
}
