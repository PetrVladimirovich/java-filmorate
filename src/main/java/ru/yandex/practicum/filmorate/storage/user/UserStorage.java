package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserStorage {
    void add(User user);

    void delete(int id);

    void update(User user);

    List<User> allUsers();

    User getUserById(int id);
}
