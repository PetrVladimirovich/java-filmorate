package ru.yandex.practicum.filmorate.storage.user;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.AbstractStorage;

import java.util.List;
import java.util.Optional;
import java.util.Set;


public interface UserStorage extends AbstractStorage<User> {
    List<User> allUsers();
    Optional<User> getUserById(int id);
}
