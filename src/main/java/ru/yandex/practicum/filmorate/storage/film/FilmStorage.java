package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmStorage {
    void add(Film film);

    void delete(int id);

    void update(Film film);

    List<Film> allFilms();

    Film getFilmById(int id);
}