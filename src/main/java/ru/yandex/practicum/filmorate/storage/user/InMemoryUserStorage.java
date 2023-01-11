package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;

@Component("InMemoryUserStorage")
@Slf4j
public class InMemoryUserStorage implements UserStorage {
    private final Map<Integer, User> users = new HashMap<>();

    private static Integer id = 1;

    @Override
    public User add(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        user.setId(id++);
        users.put(user.getId(), user);
        log.info("В хранилище добавлен пользователь '{}' с id: '{}'", user.getName(), user.getId());
        return user;
    }

    @Override
    public void delete(User user) {
        users.remove(user.getId());
        log.info("Из хранилища удалён пользователь '{}' с id: '{}'", user.getName(), user.getId());
    }

    @Override
    public User update(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        if (users.containsKey(user.getId())) {
            users.put(user.getId(), user);
            log.info("В хранилище обновлён пользователь '{}' с id: '{}'", user.getName(), user.getId());
        }else {
            log.error("В хранилище не было пользователя '{}' с id: '{}'", user.getName(), user.getId());
            throw new NotFoundException("Такого пользователя нету!");
        }
        return user;
    }

    @Override
    public Optional<User> getUserById(int id) {
        if (users.containsKey(id)) {
            return Optional.of(users.get(id));
        }else {
            return Optional.empty();
        }

    }

    public Map<Integer, User> getUsers() {
        return users;
    }

    @Override
    public List<User> allUsers() {
        return new ArrayList<User>(users.values());
    }


    public Set<Long> getUserFriendsById(Integer userId) {
        if (users.containsKey(userId)) {
            return users.get(userId).getFriends();
        }else {
            throw new NotFoundException(String.format("Такого пользователя с id: %d нету!", userId));
        }
    }

}
