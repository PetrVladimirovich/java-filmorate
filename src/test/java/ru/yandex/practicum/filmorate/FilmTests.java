package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.*;
import java.time.LocalDate;
import java.util.Collections;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class FilmTests {
    Film film;
    Set<ConstraintViolation<Film>> violations;
    Validator validator;
    ValidatorFactory factory;

    @BeforeEach
    public void beforeEach() {
        factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void invalidName() {
        film = Film.builder().name("")
                    .description("qweq")
                    .duration(120)
                    .releaseDate(LocalDate.of(1999, 12, 12))
                    .build();
        violations = validator.validate(film);

        assertFalse(violations.isEmpty(), violations.toString());
        violations.clear();
    }

    @Test
    public void lengthDescription() {
        film = Film.builder().name("qwe")
                .description(String.join("", Collections.nCopies(201, "a")))
                .duration(120)
                .releaseDate(LocalDate.of(1999, 12, 12))
                .build();
        violations = validator.validate(film);

        assertFalse(violations.isEmpty());
        violations.clear();

        film = Film.builder().name("qwe")
                .description(String.join("", Collections.nCopies(200, "a")))
                .duration(120)
                .releaseDate(LocalDate.of(1999, 12, 12))
                .build();

        assertTrue(violations.isEmpty());
        violations.clear();

        film = Film.builder().name("qwe")
                .description(String.join("", Collections.nCopies(0, "a")))
                .duration(120)
                .releaseDate(LocalDate.of(1999, 12, 12))
                .build();

        assertTrue(violations.isEmpty());
        violations.clear();
    }

    @Test
    public void invalidReleaseDate() {
        film = Film.builder().name("qwe")
                .description(String.join("", Collections.nCopies(100, "a")))
                .duration(120)
                .releaseDate(LocalDate.of(1895, 12, 12))
                .build();
        assertTrue(film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28)));

        film = Film.builder().name("qwe")
                .description(String.join("", Collections.nCopies(100, "a")))
                .duration(120)
                .releaseDate(LocalDate.of(1895, 12, 28))
                .build();
        assertFalse(film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28)));

        film = Film.builder().name("qwe")
                .description(String.join("", Collections.nCopies(100, "a")))
                .duration(120)
                .releaseDate(LocalDate.of(2022, 12, 28))
                .build();
        assertTrue(film.getReleaseDate().isAfter(LocalDate.of(1895, 12, 28)));
    }

    @Test
    public void invalidDurationOfTheFilm() {
        film = Film.builder().name("qwe")
                .description("qweq")
                .duration(-20)
                .releaseDate(LocalDate.of(1999, 12, 12))
                .build();
        violations = validator.validate(film);

        assertFalse(violations.isEmpty(), violations.toString());
        violations.clear();
    }
}