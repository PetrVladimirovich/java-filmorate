package ru.yandex.practicum.filmorate.model;
//тестовый коментарий
import lombok.Builder;
import lombok.Data;
import ru.yandex.practicum.filmorate.exception.NotFoundException;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@Builder
public class User {
    private final Set<Long> friends = new HashSet<>();
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
