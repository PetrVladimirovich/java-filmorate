package ru.yandex.practicum.filmorate.dao.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.FactoryMakes;
import ru.yandex.practicum.filmorate.dao.GenreDao;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;
import java.util.*;

@RequiredArgsConstructor
@Component("GenreDbStorage")
public class GenreDbStorage implements GenreDao {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public Set<Genre> allGenres() {
        String sqlQuery = "SELECT * FROM genre;";
        Set<Genre> genres = new TreeSet<>(Comparator.comparing(Genre::getId));
        genres.addAll(jdbcTemplate.query(sqlQuery, (rs, rowNum) -> FactoryMakes.toGenre(rs)));
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
}
