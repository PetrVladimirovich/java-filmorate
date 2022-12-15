package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class InMemoryFilmStorage implements FilmStorage {
    private final Map<Integer, Film> films = new HashMap<>();

    @Override
    public void add(Film film) {
        films.put(film.getId(), film);
        log.info("В хранилище добавлен фильм '{}' с id: '{}'", film.getName(), film.getId());
    }

    @Override
    public void delete(int id) {
        Film film = films.remove(id);
        log.info("Из хранилища удалён фильм '{}' с id: '{}'", film.getName(), film.getId());
    }

    @Override
    public void update(Film film) {
        if (films.containsKey(film.getId())) {
            films.put(film.getId(), film);
            log.info("В хранилище обновлён фильм '{}' с id: '{}'", film.getName(), film.getId());
        }else {
            log.info("В хранилище не было фильма '{}' с id: '{}'", film.getName(), film.getId());
            throw new NotFoundException("Такого фильма нету!");
        }
    }

    @Override
    public Film getFilmById(int id) {
        if (films.containsKey(id)) {
            return films.get(id);
        }else {
            throw new NotFoundException("Фильма с ID: " + id + " нету в базе!");
        }
    }

    @Override
    public List<Film> allFilms() {
        return new ArrayList<Film>(films.values());
    }

}
