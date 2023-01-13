package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.dao.FriendDao;
import ru.yandex.practicum.filmorate.dao.impl.UserDbService;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import javax.validation.Valid;
import java.util.List;
import java.util.Set;

import static ru.yandex.practicum.filmorate.Constants.requestBodyValidationLogs;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {
    private final UserDbService userService;
    private final UserStorage userStorage;
    private final FriendDao friendDao;

    @GetMapping
    public List<User> allUsers() {
        return userStorage.allUsers();
    }

    @GetMapping("/{id}")
    public User getUserById(@PathVariable int id) {
        if (userStorage.getUserById(id).isEmpty()) {
            throw new NotFoundException("Пользователь не найден в базе");
        }
        log.info("Пользователь запрошен {}", userStorage.getUserById(id));
        return userStorage.getUserById(id).orElse(null);
    }

    @GetMapping("/{id}/friends")
    public Set<User> getFriendsOfUser(@PathVariable int id) {
        return userService.friendsOfUser(id);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    @ResponseStatus(HttpStatus.OK)
    public Set<User> getCommonFriends(@PathVariable int id, @PathVariable int otherId) {
        log.info("Общие друзья пользователей с ID: {} и ID: {} -> {}"
                , id, otherId, userService.commonFriends(id, otherId));
        return userService.commonFriends(id, otherId);
    }

    @PostMapping
    public User createUser(@Valid @RequestBody User user, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            requestBodyValidationLogs(bindingResult);
        }
        return userStorage.add(user);
    }

    @PutMapping
    public User updateUser(@Valid @RequestBody User user, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            requestBodyValidationLogs(bindingResult);
        }
        return userStorage.update(user);
    }

    @PutMapping("/{id}/friends/{friendId}")
    public void addToFriends(@PathVariable int id, @PathVariable int friendId) {
        friendDao.addToFriends(id, friendId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public void removeFromFriends(@PathVariable int id, @PathVariable int friendId) {
        log.info("Пользователи с ID: {} и ID: {} удалены друг у друга из друзей", id, friendId);
        friendDao.deleteFromFriends(id, friendId);
    }
}
