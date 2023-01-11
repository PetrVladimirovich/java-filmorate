package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.AbstractStorage;

import java.util.List;
import java.util.Optional;

public interface FilmStorage extends AbstractStorage<Film> {
        List<Film> allFilms();
        Optional<Film> getFilmById(int id);
}