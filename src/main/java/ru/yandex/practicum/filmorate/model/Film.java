package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.*;
import java.time.LocalDate;

@Data
@Builder
public class Film {

    private int id;

    @NotEmpty(message = "name не может быть пустым.")
    private final String name;

    @Size(max = 200, message = "description не может быть длиннее 200 символов.")
    private final String description;
    private final LocalDate releaseDate;

    @Positive(message = "duration не может быть отрицательным.")
    private final int duration;

}