package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.service.User;
import ru.yandex.practicum.filmorate.storage.AbstractStorage;

import java.util.List;

public interface UserStorage extends AbstractStorage<User> {
    List<User> allUsers();
    User getUserById(int id);
}
