package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.model.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.Set;

public interface GenreDao {
    Set<Genre> allGenres();

    Optional<Genre> genreById(int id);

    Set<Genre> filmGenres(Integer filmId);

    Genre makeGenre(ResultSet rs) throws SQLException;
}
