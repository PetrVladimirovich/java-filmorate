package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import java.time.LocalDate;

@Data
public class User {

    private int id;

    @Email
    @NotEmpty
    private final String email;

    @NotEmpty
    @NotBlank
    private final String login;
    private String name;
    private final LocalDate birthday;

}