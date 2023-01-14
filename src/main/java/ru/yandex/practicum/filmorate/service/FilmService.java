package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import java.util.Set;

@Service
public interface FilmService {
    Set<Film> getBestFilms(Integer count);

}
