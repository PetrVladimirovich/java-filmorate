package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;
import ru.yandex.practicum.filmorate.annotation.MinDate;
import ru.yandex.practicum.filmorate.exception.NotFoundException;

import static ru.yandex.practicum.filmorate.Constants.FILM_BIRTHDAY;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;


@Data
public class Film {
    private Set<Long> likes;
    private int id;

    @NotEmpty(message = "name не может быть пустым.")
    private String name;

    @Size(max = 200, message = "description не может быть длиннее 200 символов.")
    private String description;

    @NotNull(message = "Дата релиза обязательна!")
    @MinDate(date = FILM_BIRTHDAY)
    private LocalDate releaseDate;

    @Positive(message = "duration не может быть отрицательным.")
    private int duration;
    private Set<Genre> genres;
    private Mpa mpa;

    public void addLike(Integer userId) {
        likes.add(Long.valueOf(userId));
    }

    public void deleteLike(Integer userId) {
        if (likes.contains(Long.valueOf(userId))) {
            likes.remove(Long.valueOf(userId));
        } else {
            throw new NotFoundException("Нет лайков пользователем с ID: " + userId);
        }
    }
}