package ru.yandex.practicum.filmorate;

import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import java.sql.ResultSet;
import java.sql.SQLException;

public class FactoryMakes {
    private FactoryMakes() {};

    public static Genre toGenre(ResultSet rs) throws SQLException{
            return Genre.builder()
                    .id(rs.getInt("id"))
                    .name(rs.getString("name"))
                    .build();
    }

    public static Mpa toMpa(ResultSet rs) throws SQLException {
        return Mpa.builder()
                .id(rs.getInt("id"))
                .name(rs.getString("name"))
                .build();
    }
}