package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.dao.GenreDao;
import ru.yandex.practicum.filmorate.model.Genre;
import java.util.Set;

@RestController
@Slf4j
@RequiredArgsConstructor
public class GenreController {
    private final GenreDao genreService;

    @GetMapping("/genres")
    public Set<Genre> findAll() {
        log.info("Список всех жанров");
        return genreService.allGenres();
    }

    @GetMapping("/genres/{id}")
    public Genre findById(@PathVariable Integer id) {
        log.info("Жанр с ID: {}", id);
        return genreService.genreById(id).orElse(null);
    }
}