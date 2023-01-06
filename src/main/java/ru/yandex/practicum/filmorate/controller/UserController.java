package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.service.User;
import ru.yandex.practicum.filmorate.service.UserService;

import javax.validation.Valid;
import java.util.List;
import java.util.Set;

import static ru.yandex.practicum.filmorate.Constants.requestBodyValidationLogs;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    @GetMapping
    public List<User> allUsers() {
        return userService.allUsers();
    }

    @GetMapping("/{id}")
    public User getUserById(@PathVariable int id) {
        if (userService.getUserById(id) == null) {
            throw new NotFoundException("Пользователь не найден в базе");
        }
        log.info("Пользователь запрошен {}", userService.getUserById(id));
        return userService.getUserById(id);
    }

    @GetMapping("/{id}/friends")
    public Set<User> getFriendsOfUser(@PathVariable int id) {
        return userService.getFriendsOfUser(id);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    @ResponseStatus(HttpStatus.OK)
    public Set<User> getCommonFriends(@PathVariable int id, @PathVariable int otherId) {
        log.info("Общие друзья пользователей с id-{} и id-{} : {}"
                , id, otherId, userService.getCommonFriends(id, otherId));
        return userService.getCommonFriends(id, otherId);
    }

    @PostMapping
    public User createUser(@Valid @RequestBody User user, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            requestBodyValidationLogs(bindingResult);
        }
        return userService.createUser(user);
    }

    @PutMapping
    public User updateUser(@Valid @RequestBody User user, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            requestBodyValidationLogs(bindingResult);
        }
        return userService.updateUser(user);
    }

    @PutMapping("/{id}/friends/{friendId}")
    public void addToFriends(@PathVariable int id, @PathVariable int friendId) {
        userService.addToFriends(id, friendId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public void removeFromFriends(@PathVariable int id, @PathVariable int friendId) {
        log.info("Пользователи с ID: {} и ID: {} удалены друг у друга из друзей", id, friendId);
        userService.removeFromFriends(id, friendId);
    }
}
