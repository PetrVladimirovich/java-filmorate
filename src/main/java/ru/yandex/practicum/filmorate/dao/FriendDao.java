package ru.yandex.practicum.filmorate.dao;

public interface FriendDao {
    void addToFriends(Integer userId, Integer friendId);
    void deleteFromFriends(Integer userId, Integer friendId);
}
