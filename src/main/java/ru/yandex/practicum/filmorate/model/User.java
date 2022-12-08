package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.*;
import java.time.LocalDate;

@Data
@Builder
public class User {

    private int id;

    @Email(message = "Неверный формат Email.")
    @NotEmpty(message = "email не может быть пустым.")
    private final String email;

    @NotBlank(message = "login не может содержать пробелы.")
    @NotEmpty(message = "login не может быть пустым.")
    private final String login;
    private String name;

    @Past(message = "birthday не может быть позже текущей даты.")
    private final LocalDate birthday;

}