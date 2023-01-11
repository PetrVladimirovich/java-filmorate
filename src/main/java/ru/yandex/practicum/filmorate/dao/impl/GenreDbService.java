package ru.yandex.practicum.filmorate.dao.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.GenreDao;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@RequiredArgsConstructor
@Component("GenreDbService")
public class GenreDbService implements GenreDao {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public Set<Genre> allGenres() {
        String sqlQuery = "SELECT * FROM genre;";
        Set<Genre> genres = new TreeSet<>(Comparator.comparing(Genre::getId));
        genres.addAll(jdbcTemplate.query(sqlQuery, (rs, rowNum) -> makeGenre(rs)));
        return genres;
    }

    @Override
    public Optional<Genre> genreById(int id) {
        String sqlQuery = "SELECT * FROM genre WHERE id = ?;";
        SqlRowSet genres = jdbcTemplate.queryForRowSet(sqlQuery, id);
        if (genres.next()) {
            return Optional.of(Genre.builder()
                    .id(genres.getInt("id"))
                    .name(genres.getString("name"))
                    .build());
        } else {
            throw new NotFoundException(String.format("Жанра с ID: %d нет в БД!", id));
        }
    }

    @Override
    public Set<Genre> filmGenres(Integer filmId) {
        String sqlQuery = "SELECT * FROM film_genre AS fg " +
                "INNER JOIN genre AS gen ON fg.genre_id = gen.id " +
                "WHERE film_id = ?";
        return new HashSet<>(jdbcTemplate.query(sqlQuery, (rs, rowNum) -> makeGenre(rs), filmId));
    }

    @Override
    public Genre makeGenre(ResultSet rs) throws SQLException {
        return Genre.builder()
                .id(rs.getInt("id"))
                .name(rs.getString("name"))
                .build();
    }
}
