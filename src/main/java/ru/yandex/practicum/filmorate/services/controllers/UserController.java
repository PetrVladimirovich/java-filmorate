package ru.yandex.practicum.filmorate.services.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@Slf4j
public class UserController {
    private final Map<Integer, User> users = new HashMap<>();
    private int id = 1;

    @GetMapping("/users")
    public List<User> allUsers() {
        return new ArrayList<User>(users.values());
    }

    @PostMapping("/users")
    public User createUser(@Valid @RequestBody User user, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            List<FieldError> errors = bindingResult.getFieldErrors();
            List<String> message = new ArrayList<>();
            for (FieldError e : errors) {
                message.add("@" + e.getField().toUpperCase() + " : " + e.getDefaultMessage() + "\n");
            }

            log.info("Недопустимые значения полей : " + message);
            throw new ValidationException("Недопустимые значения полей");
        }
        if (user.getName() == null) {
            user.setName(user.getLogin());
        }

        user.setId(id);
        users.put(id++, user);
        log.info("Пользователь добавлен! Ему присвоен id: '{}'", user.getId());

        return user;
    }

    @PutMapping("/users")
    public User updateUser(@Valid @RequestBody User user, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            List<FieldError> errors = bindingResult.getFieldErrors();
            List<String> message = new ArrayList<>();
            for (FieldError e : errors) {
                message.add("@" + e.getField().toUpperCase() + " : " + e.getDefaultMessage() + "\n");
            }

            log.info("Недопустимые значения полей : " + message);
            throw new ValidationException("Недопустимые значения полей");
        }

        if (user.getName() == null) {
            user.setName(user.getLogin());
        }

        if (users.containsKey(user.getId())) {
            users.put(user.getId(), user);
            log.info("Пользователь с данным id: '{}', изменён!", user.getId());

            return user;
        }

        log.info("Пользователя с данным id: '{}' нету в базе!", user.getId());
        throw new ValidationException("Нету такого пользователя");
    }
}
