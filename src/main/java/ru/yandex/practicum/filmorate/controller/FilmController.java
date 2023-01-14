package ru.yandex.practicum.filmorate.controller;

import javax.validation.Valid;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.dao.LikeDao;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.util.List;
import java.util.Set;

import static ru.yandex.practicum.filmorate.Constants.requestBodyValidationLogs;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/films")
public class FilmController {

    private final FilmService filmService;
    private final FilmStorage filmStorage;
    private final LikeDao likeDao;

    @GetMapping
    public List<Film> allFilms() {
        return filmStorage.allFilms();
    }

    @GetMapping("/{id}")
    public Film getFilmById(@PathVariable int id) {
        if (filmStorage.getFilmById(id).isEmpty()) {
            throw new NotFoundException("Фильм не найден в базе!");
        }
        return filmStorage.getFilmById(id).orElse(null);
    }

    @GetMapping("/popular")
    public Set<Film> getPopular(@RequestParam(defaultValue = "10", required = false) int count) {
        Set<Film> bestFilms = filmService.getBestFilms(count);
        log.info("Самые популярные(х) {} фильмы в базе: {}", count, bestFilms);
        return bestFilms;
    }

    @PostMapping
    public Film createFilm(@Valid @RequestBody Film film, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            requestBodyValidationLogs(bindingResult);
        }
        return filmStorage.add(film);
    }

    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film film, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            requestBodyValidationLogs(bindingResult);
        }
        return filmStorage.update(film);
    }

    @PutMapping("/{id}/like/{userId}")
    public void addLike(@PathVariable int id, @PathVariable int userId) {
        likeDao.addLike(userId, id);
    }

    @DeleteMapping("/{id}/like/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteLike(@PathVariable int id, @PathVariable int userId) {
        likeDao.deleteLike(userId, id);
    }
}
