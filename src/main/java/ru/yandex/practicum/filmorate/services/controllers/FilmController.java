package ru.yandex.practicum.filmorate.services.controllers;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.time.chrono.ChronoLocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@Slf4j
public class FilmController {
    final Map<Integer, Film> films = new HashMap<>();
    int id = 1;

    @GetMapping("/films")
    public List<Film> allFilms() {
        return new ArrayList<Film>(films.values());
    }

    @PostMapping("/films")
    public Film createFilm(@Valid @RequestBody Film film, BindingResult bindingResult) {
        if (bindingResult.hasErrors() || film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))){
            log.info("Недопустимые поля для фильма!");
            throw new ValidationException("Недопустимые поля для фильма!");
        }

        film.setId(id);
        films.put(id++, film);
        log.info("Фильм добавлен! Ему присвоен id: '{}'", film.getId());

        return film;
    }

    @PutMapping("/films")
    public Film updateFilm(@Valid @RequestBody Film film, BindingResult bindingResult) {
        if (bindingResult.hasErrors() || film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            log.info("Недопустимые поля для фильма!");
            throw new ValidationException("Недопустимые поля для фильма!");
        }

        if (films.containsKey(film.getId())) {
            films.put(film.getId(), film);
            log.info("Фильм с данным id: '{}', изменён!", film.getId());

            return film;
        }

        log.info("Фильма с данным id: '{}' нету в базе!", film.getId());
        throw new ValidationException("Нету такого фильма");
    }
}
