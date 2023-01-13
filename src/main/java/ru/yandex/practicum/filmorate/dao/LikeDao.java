package ru.yandex.practicum.filmorate.dao;

public interface LikeDao {
    void deleteLike(Integer userId, Integer filmId);
    void addLike(Integer userId, Integer filmId);
}