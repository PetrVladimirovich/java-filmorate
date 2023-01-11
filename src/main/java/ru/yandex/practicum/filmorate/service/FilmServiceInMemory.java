package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.relational.core.sql.In;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.film.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@Slf4j
@RequiredArgsConstructor
public class FilmServiceInMemory implements FilmService{
    private final InMemoryFilmStorage filmStorage;
    private final InMemoryUserStorage userStorage;
    @Override
    public void addLike(Integer userId, Integer filmId) {
         if(getUserById(userId).isPresent() && getFilmById(filmId).isPresent()) {
             getFilmById(filmId).get().addLike(userId);
             log.info("Пользователь c id: {} поставил фильму с id: {} лайкосик", userId, filmId);
         }else {
             throw new NotFoundException(String.format("Пользователя с id: %d или фильма с id: %d нету в БД!", userId, filmId));
         }
    }
    @Override
    public void deleteLike(Integer userId, Integer filmId) {
        if(getUserById(userId).isPresent() && getFilmById(filmId).isPresent()) {
            getFilmById(filmId).get().deleteLike(userId);
            log.info("Пользователь c id: {} удалил фильму с id: {} лайкосик", userId, filmId);
        }else {
            throw new NotFoundException(String.format("Пользователя с id: %d или фильма с id: %d нету в БД!", userId, filmId));
        }
    }
    @Override
    public Set<Film> getBestFilms(Integer count) {
            return filmStorage.allFilms().stream()
                .sorted(this::compare)
                .limit(count)
                .collect(Collectors.toSet());
    }

    private Optional<Film> getFilmById(int id) {
        return filmStorage.getFilmById(id);
    }

    private Optional<User> getUserById(int id) {
        return userStorage.getUserById(id);
    }

    private int compare(Film firstFilm, Film secondFilm) {
        int result = firstFilm.getLikes().size() - secondFilm.getLikes().size();
        result = -1 * result;
        return result;
    }
}