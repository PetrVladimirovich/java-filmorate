package ru.yandex.practicum.filmorate.controller;

import javax.validation.Valid;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import java.util.List;
import java.util.Set;

import static ru.yandex.practicum.filmorate.Constants.requestBodyValidationLogs;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/films")
public class FilmController {

    private final FilmService filmService;

    @GetMapping
    public List<Film> allFilms() {
        return filmService.allFilms();
    }

    @GetMapping("/{id}")
    public Film getFilmById(@PathVariable int id) {
        if (filmService.getFilmById(id) == null) {
            throw new NotFoundException("Фильм не найден в базе");
        }
        return filmService.getFilmById(id);
    }

    @GetMapping("/popular")
    public Set<Film> getPopular(@RequestParam(defaultValue = "10", required = false) int count) {
        log.info("Самые популярные {} фильмы в базе: {}", count, filmService.getBestFilms(count));
        return filmService.getBestFilms(count);
    }

    @PostMapping
    public Film createFilm(@Valid @RequestBody Film film, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            requestBodyValidationLogs(bindingResult);
        }
        return filmService.createFilm(film);
    }

    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film film, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            requestBodyValidationLogs(bindingResult);
        }
        return filmService.updateFilm(film);
    }

    @PutMapping("/{id}/like/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public void addLike(@PathVariable int id, @PathVariable int userId) {
        filmService.addLike(userId, id);
    }

    @DeleteMapping("/{id}/like/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteLike(@PathVariable int id, @PathVariable int userId) {
        filmService.deleteLike(userId, id);
    }
}
