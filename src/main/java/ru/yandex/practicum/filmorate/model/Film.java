package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.Duration;
import java.time.LocalDate;

@Data
public class Film {

    private int id;

    @NotEmpty
    private final String name;

    @Size(max = 200)
    private final String description;
    private final LocalDate releaseDate;

    @Min(value = 0)
    private final int duration;

}
