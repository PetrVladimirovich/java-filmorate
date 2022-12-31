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
@Builder
public class Film {
    private final Set<Long> likes = new HashSet<>();
    private int id;

    @NotEmpty(message = "name не может быть пустым.")
    private final String name;

    @Size(max = 200, message = "description не может быть длиннее 200 символов.")
    private final String description;

    @NotNull(message = "Дата релиза обязательна!")
    @MinDate(date = FILM_BIRTHDAY)
    private final LocalDate releaseDate;

    @Positive(message = "duration не может быть отрицательным.")
    private final int duration;
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