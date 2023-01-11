package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import java.util.Set;

@Service
public interface FilmService {
    void addLike(Integer userId, Integer filmId);

    void deleteLike(Integer userId, Integer filmId);
    Set<Film> getBestFilms(Integer count);

}
