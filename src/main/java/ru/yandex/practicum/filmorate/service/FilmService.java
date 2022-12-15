package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class FilmService {
    private final FilmStorage filmStorage;
    private static Integer id = 1;

    @Autowired
    public FilmService(FilmStorage filmStorage) {
        this.filmStorage = filmStorage;
    }

    public void addLike(Integer userId, Integer filmId) {
          Film film = filmStorage.getFilmById(filmId);
          film.addLike(userId);
          filmStorage.update(film);
    }

    public void deleteLike(Integer userId, Integer filmId) {
        Film film = filmStorage.getFilmById(filmId);
        film.deleteLike(userId);
        filmStorage.update(film);
    }

    public Set<Film> getBestFilms(Integer count) {
        return filmStorage.allFilms().stream()
                .sorted(this::compare)
                .limit(count)
                .collect(Collectors.toSet());
    }

    public Film getFilmById(int id) {
        return filmStorage.getFilmById(id);
    }

    private int compare(Film firstFilm, Film secondFilm) {
        int result = firstFilm.getLikes().size() - secondFilm.getLikes().size();
        result = -1 * result;
        return result;
    }

    public Film createFilm(Film film) {
        film.setId(id++);
        filmStorage.add(film);
        return film;
    }

    public List<Film> allFilms() {
        return filmStorage.allFilms();
    }

    public Film updateFilm(Film film) {
        filmStorage.update(film);
        return film;
    }

}