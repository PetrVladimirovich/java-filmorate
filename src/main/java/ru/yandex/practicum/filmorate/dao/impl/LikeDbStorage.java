package ru.yandex.practicum.filmorate.dao.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.LikeDao;
import ru.yandex.practicum.filmorate.exception.NotFoundException;

@Component("LikeDbStorage")
@RequiredArgsConstructor
public class LikeDbStorage implements LikeDao {
    private final JdbcTemplate jdbcTemplate;
    private final FilmDbStorage filmDbStorage;
    private final UserDbStorage userDbStorage;

    @Override
    public void deleteLike(Integer userId, Integer filmId) {
        if(userDbStorage.getUserById(userId).isPresent() &&
                filmDbStorage.getFilmById(filmId).isPresent()) {
            String sqlQuery = "DELETE FROM film_likes WHERE film_id = ? AND liked_from_id = ?;";
            jdbcTemplate.update(sqlQuery, filmId, userId);
        } else {
            throw new NotFoundException("Некоректные данные.");
        }
    }

    @Override
    public void addLike(Integer userId, Integer filmId) {
        if(userDbStorage.getUserById(userId).isPresent() &&
                filmDbStorage.getFilmById(filmId).isPresent()) {
            String sqlQuery = "INSERT INTO film_likes (film_id,liked_from_id) VALUES (?,?);";
            jdbcTemplate.update(sqlQuery, filmId, userId);
        } else {
            throw new NotFoundException("Некоректные данные.");
        }
    }
}
