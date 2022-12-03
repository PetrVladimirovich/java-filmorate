package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Past;
import lombok.Builder;
import lombok.Data;
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