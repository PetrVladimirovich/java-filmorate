package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class InMemoryUserStorage implements UserStorage {
    private final Map<Integer, User> users = new HashMap<>();

    @Override
    public void add(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        users.put(user.getId(), user);
        log.info("В хранилище добавлен пользователь '{}' с id: '{}'", user.getName(), user.getId());
    }

    @Override
    public void delete(int id) {
        User user = users.remove(id);
        log.info("Из хранилища удалён пользователь '{}' с id: '{}'", user.getName(), user.getId());
    }

    @Override
    public void update(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        if (users.containsKey(user.getId())) {
            users.put(user.getId(), user);
            log.info("В хранилище обновлён пользователь '{}' с id: '{}'", user.getName(), user.getId());
        }else {
            log.info("В хранилище не было пользователя '{}' с id: '{}'", user.getName(), user.getId());
            throw new NotFoundException("Такого пользователя нету!");
        }
    }

    @Override
    public User getUserById(int id) {
        if (users.containsKey(id)) {
            return users.get(id);
        }else {
            throw new NotFoundException("Пользователя с ID: " + id + " нету в базе!");
        }

    }

    @Override
    public List<User> allUsers() {
        return new ArrayList<User>(users.values());
    }
}
