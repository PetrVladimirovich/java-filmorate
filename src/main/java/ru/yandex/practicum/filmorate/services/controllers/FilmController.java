package ru.yandex.practicum.filmorate.services.controllers;

import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@Slf4j
public class FilmController {
    private final Map<Integer, Film> films = new HashMap<>();
    private int id = 1;

    @GetMapping("/films")
    public List<Film> allFilms() {
        return new ArrayList<Film>(films.values());
    }

    @PostMapping("/films")
    public Film createFilm(@Valid @RequestBody Film film, BindingResult bindingResult) {
        if (bindingResult.hasErrors() || film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {

            List<FieldError> errors = bindingResult.getFieldErrors();
            List<String> message = new ArrayList<>();
            for (FieldError e : errors) {
                message.add("@" + e.getField().toUpperCase() + " : " + e.getDefaultMessage() + "\n");
            }

            log.info("Недопустимые значения полей: " + message);
            throw new ValidationException("Недопустимые значения полей");
        }

        film.setId(id);
        films.put(id++, film);
        log.info("Фильм добавлен! Ему присвоен id: '{}'", film.getId());

        return film;
    }

    @PutMapping("/films")
    public Film updateFilm(@Valid @RequestBody Film film, BindingResult bindingResult) {
        if (bindingResult.hasErrors() || film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {

            List<FieldError> errors = bindingResult.getFieldErrors();
            List<String> message = new ArrayList<>();
            for (FieldError e : errors) {
                message.add("@" + e.getField().toUpperCase() + " : " + e.getDefaultMessage() + "\n");
            }

            log.info("Недопустимые значения полей: " + message);
            throw new ValidationException("Недопустимые значения полей");
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
