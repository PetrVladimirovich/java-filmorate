package ru.yandex.practicum.filmorate.dao.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import java.util.HashSet;
import java.util.Set;

@Service("FilmDbService")
@RequiredArgsConstructor
@Primary
public class FilmDbService implements FilmService {
    private final JdbcTemplate jdbcTemplate;
    private final FilmDbStorage filmDbStorage;

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