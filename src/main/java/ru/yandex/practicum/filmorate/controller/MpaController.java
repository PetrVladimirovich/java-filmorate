package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.dao.MpaDao;
import ru.yandex.practicum.filmorate.model.Mpa;
import java.util.Set;

@RestController
@Slf4j
@RequiredArgsConstructor
public class MpaController {
    private final MpaDao mpaService;

    @GetMapping("/mpa")
    public Set<Mpa> findAll() {
        log.info("Список всех возврастных ограничений!");
        return mpaService.allMpa();
    }

    @GetMapping("/mpa/{id}")
    public Mpa findById(@PathVariable Integer id) {
        log.info("Жанр с ID: {}", id);
        return mpaService.mpaById(id).orElse(null);
    }
}