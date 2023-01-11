package ru.yandex.practicum.filmorate.dao.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Slf4j
@Component("FilmDbStorage")
@Primary
@RequiredArgsConstructor
public class FilmDbStorage implements FilmStorage {
    private final JdbcTemplate jdbcTemplate;
    private final MpaDbService mpaDbService;
    private final GenreDbService genreDbService;

    @Override
    public Film add(Film film) {
        String sqlQuery = "INSERT INTO film (name, description, release_date, duration, mpa_id) VALUES (?, ?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement statement = connection.prepareStatement(sqlQuery, new String[]{"id"});
            statement.setString(1, film.getName());
            statement.setString(2, film.getDescription());
            statement.setDate(3, Date.valueOf(film.getReleaseDate()));
            statement.setInt(4, film.getDuration());
            statement.setInt(5, film.getMpa().getId());
            return statement;
        }, keyHolder);

        String secondSqlQuery = "MERGE INTO film_genre (film_id,genre_id) VALUES (?,?)";
        if(film.getGenres() != null && !film.getGenres().isEmpty()) {
            for (Genre g : film.getGenres()) {
                jdbcTemplate.update(connection -> {
                    PreparedStatement statement = connection.prepareStatement(secondSqlQuery);
                    statement.setInt(1, Objects.requireNonNull(keyHolder.getKey()).intValue());
                    statement.setInt(2, g.getId());
                    return statement;
                });
            }
        }
        return getFilmById(Objects.requireNonNull(keyHolder.getKey()).intValue()).orElse(null);
    }

    @Override
    public Film update(Film film) {
        if (getFilmById(film.getId()).orElse(null) == null) {
            throw new NotFoundException(String.format("Фильма с ID: %d нету в БД!", film.getId()));
        } else {
            String sqlQuery = "UPDATE film SET name = ?, description = ?, release_date = ?, duration = ?, mpa_id = ?" +
                    " WHERE id = ?;";
            jdbcTemplate.update(
                    sqlQuery,
                    film.getName(),
                    film.getDescription(),
                    film.getReleaseDate(),
                    film.getDuration(),
                    film.getMpa().getId(),
                    film.getId());

            String sql1 = "DELETE FROM film_genre WHERE film_id = ?;";
            jdbcTemplate.update(sql1, film.getId());

            String secondSqlQuery = "MERGE INTO film_genre (film_id,genre_id) VALUES (?,?);";

            if (film.getGenres() != null && !film.getGenres().isEmpty()) {
                for (Genre g : film.getGenres()) {
                    jdbcTemplate.update(connection -> {
                        PreparedStatement statement = connection.prepareStatement(secondSqlQuery);
                        statement.setInt(1, film.getId());
                        statement.setInt(2, g.getId());
                        return statement;
                    });
                }
            }
        }
        return getFilmById(film.getId()).orElse(null);
    }

    @Override
    public void delete(Film film) {
        String sqlQuery = "DELETE FROM film WHERE id = ?;";
        jdbcTemplate.update(sqlQuery, film.getId());
    }

    @Override
    public List<Film> allFilms() {
        String sqlQuery =
                "SELECT * FROM film AS f INNER JOIN mpa AS m ON m.id = f.mpa_id;";
        return jdbcTemplate.query(sqlQuery, (rs, rowNum) -> makeFilm(rs));
    }

    @Override
    public Optional<Film> getFilmById(int id) {
        String sqlQuery =
                "SELECT * FROM film AS f INNER JOIN mpa AS m ON m.id = f.mpa_id WHERE f.id = ?";

        SqlRowSet filmRows = jdbcTemplate.queryForRowSet(sqlQuery, id);

        if (filmRows.next()) {
            Film film = new Film();
            film.setLikes(getFilmLikes(filmRows.getInt("id")));
            film.setId(filmRows.getInt("id"));
            film.setName(filmRows.getString("name"));
            film.setDescription(filmRows.getString("description"));
            film.setReleaseDate(Objects.requireNonNull(filmRows.getDate("release_date")).toLocalDate());
            film.setDuration(filmRows.getInt("duration"));
            film.setMpa(mpaOption(filmRows.getInt("mpa_id")));
            film.setGenres(genreDbService.filmGenres(filmRows.getInt("id")));

            log.info("Найден фильм: {} {}", film.getName(), id);
            return Optional.of(film);
        } else {
            log.info("Фильм с идентификатором {} не найден.", id);
            return Optional.empty();
        }
    }

    private Mpa mpaOption(int id) {
        if (mpaDbService.mpaById(id).isPresent()) {
            return mpaDbService.mpaById(id).get();
        }else {
            return null;
        }
    }

    private Set<Long> getFilmLikes(Integer filmId) {
        String sql = "SELECT * FROM film_likes WHERE film_id = ?";
        return new HashSet<>(jdbcTemplate.query(sql, (rs, rowNum) -> rs.getLong("liked_from_id"), filmId));
    }

    public Film makeFilm(ResultSet rs) throws SQLException, NotFoundException {
        Film film = new Film();
        film.setLikes(getFilmLikes(rs.getInt("id")));
        film.setId(rs.getInt("id"));
        film.setName(rs.getString("name"));
        film.setDescription(rs.getString("description"));
        film.setReleaseDate(Objects.requireNonNull(rs.getDate("release_date")).toLocalDate());
        film.setDuration(rs.getInt("duration"));
        film.setMpa(mpaOption(rs.getInt("mpa_id")));
        film.setGenres(genreDbService.filmGenres(rs.getInt("id")));
        return film;
    }
}