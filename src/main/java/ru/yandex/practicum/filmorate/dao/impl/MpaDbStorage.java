package ru.yandex.practicum.filmorate.dao.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.FactoryMakes;
import ru.yandex.practicum.filmorate.dao.MpaDao;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;
import java.util.Comparator;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;

@RequiredArgsConstructor
@Component("MpaDbStorage")
public class MpaDbStorage implements MpaDao {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public Set<Mpa> allMpa() {
        String sqlQuery = "SELECT * FROM mpa;";
        Set<Mpa> mpas = new TreeSet<>(Comparator.comparing(Mpa::getId));
        mpas.addAll(jdbcTemplate.query(sqlQuery, (rs, rowNum) -> FactoryMakes.toMpa(rs)));
        return mpas;
    }

    @Override
    public Optional<Mpa> mpaById(int id) {
        String sqlQuery = "SELECT * FROM mpa WHERE id = ?;";
        SqlRowSet mpas = jdbcTemplate.queryForRowSet(sqlQuery, id);
        if (mpas.next()) {
            return Optional.of(Mpa.builder()
                    .id(mpas.getInt("id"))
                    .name(mpas.getString("name"))
                    .build());
        } else {
            throw new NotFoundException(String.format("Возрастного ограничения с ID: %d нет в БД!", id));
        }
    }
}