package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;
import ru.yandex.practicum.filmorate.annotation.NoSpace;
import ru.yandex.practicum.filmorate.exception.NotFoundException;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.Set;

@Data
public class User {
    private Set<Long> friends;
    private int id;

    @Email(message = "Неверный формат Email.")
    @NotEmpty(message = "email не может быть пустым.")
    private String email;

    @NoSpace
    @NotEmpty(message = "login не может быть пустым.")
    private String login;
    private String name;

    @Past(message = "birthday не может быть позже текущей даты.")
    private LocalDate birthday;

    public void addFriend(Integer userId) {
        friends.add(Long.valueOf(userId));
    }

    public void deleteFriend(Integer userId) {
        if(friends.contains(Long.valueOf(userId))) {
            friends.remove(Long.valueOf(userId));
        } else {
            throw new NotFoundException("Пользователя с ID: " + userId + " нет в друзьях у пользователя: " + getName());
        }
    }
}
