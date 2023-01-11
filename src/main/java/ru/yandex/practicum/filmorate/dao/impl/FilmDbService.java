package ru.yandex.practicum.filmorate.dao.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import java.util.HashSet;
import java.util.Set;

@Service("FilmDbService")
@Slf4j
@RequiredArgsConstructor
@Primary
public class FilmDbService implements FilmService {
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

    @Override
    public Set<Film> getBestFilms(Integer count) {
        String sqlQuery = "SELECT * FROM film AS f INNER JOIN mpa AS m ON f.mpa_id = m.id " +
                          "WHERE f.id IN (SELECT film_id FROM film_likes GROUP BY film_id " +
                          "ORDER BY COUNT(liked_from_id) DESC LIMIT(?));";

        if(!jdbcTemplate.query(sqlQuery, (rs, rowNum) -> filmDbStorage.makeFilm(rs), count).isEmpty()) {
           return new HashSet<>(jdbcTemplate.query(sqlQuery, (rs, rowNum) -> filmDbStorage.makeFilm(rs), count));
        } else {
            String secondSqlQuery = "SELECT * FROM film AS f INNER JOIN mpa AS m ON f.mpa_id = m.id LIMIT(?);";
            return new HashSet<>(jdbcTemplate.query(secondSqlQuery, (rs, rowNum) -> filmDbStorage.makeFilm(rs), count));
        }
    }

}