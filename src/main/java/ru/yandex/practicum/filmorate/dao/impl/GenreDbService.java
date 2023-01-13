package ru.yandex.practicum.filmorate.dao.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.FactoryMakes;
import ru.yandex.practicum.filmorate.model.Genre;
import java.util.HashSet;
import java.util.Set;

@Service("GenreService")
@RequiredArgsConstructor
public class GenreDbService {
    private final JdbcTemplate jdbcTemplate;
    public Set<Genre> filmGenres(Integer filmId) {
        String sqlQuery = "SELECT * FROM film_genre AS fg " +
                "INNER JOIN genre AS gen ON fg.genre_id = gen.id " +
                "WHERE film_id = ?";
        return new HashSet<>(jdbcTemplate.query(sqlQuery, (rs, rowNum) -> FactoryMakes.toGenre(rs), filmId));
    }
}
