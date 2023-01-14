package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.*;

@Component("InMemoryFilmStorage")
@Slf4j
public class InMemoryFilmStorage implements FilmStorage {
    private final Map<Integer, Film> films = new HashMap<>();

    private static Integer id = 1;
    @Override
    public Film add(Film film) {
        film.setId(id++);
        films.put(film.getId(), film);
        log.info("В хранилище добавлен фильм '{}' с id: '{}'", film.getName(), film.getId());
        return film;
    }

    @Override
    public void delete(Film film) {
        films.remove(film.getId());
        log.info("Из хранилища удалён фильм '{}' с id: '{}'", film.getName(), film.getId());
    }

    @Override
    public Film update(Film film) {
        if (films.containsKey(film.getId())) {
            films.put(film.getId(), film);
            log.info("В хранилище обновлён фильм '{}' с id: '{}'", film.getName(), film.getId());
        }else {
            log.error("В хранилище не было фильма '{}' с id: '{}'", film.getName(), film.getId());
            throw new NotFoundException("Такого фильма нету!");
        }

        return film;
    }

    @Override
    public Optional<Film> getFilmById(int id) {
        if (films.containsKey(id)) {
            return Optional.of(films.get(id));
        }else {
            return Optional.empty();
        }
    }

    @Override
    public List<Film> allFilms() {
        return new ArrayList<Film>(films.values());
    }

}
