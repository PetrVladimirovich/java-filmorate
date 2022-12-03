package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.*;
import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class UserTests {
    User user;
    Set<ConstraintViolation<User>> violations;
    Validator validator;
    ValidatorFactory factory;


    @BeforeEach
    public void beforeEach() {
        factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void invalidEmail() {
       user = User.builder()
               .email("")
               .login("peter")
               .name("Peter")
               .birthday(LocalDate.of(1991, 3, 3))
               .build();
        violations = validator.validate(user);

        assertFalse(violations.isEmpty());
        violations.clear();

        user = User.builder()
                .email("qweqwew")
                .login("peter")
                .name("Peter")
                .birthday(LocalDate.of(1991, 3, 3))
                .build();
        violations = validator.validate(user);

        assertFalse(violations.isEmpty());
        violations.clear();

        user = User.builder()
                .email("dzeensibir@yandex.ru")
                .login("peter")
                .name("Peter")
                .birthday(LocalDate.of(1991, 3, 3))
                .build();
        violations = validator.validate(user);

        assertTrue(violations.isEmpty());
        violations.clear();
    }

    @Test
    public void invalidLogin() {
        user = User.builder()
                .email("dzeensibir@yandex.ru")
                .login("")
                .name("Peter")
                .birthday(LocalDate.of(1991, 3, 3))
                .build();
        violations = validator.validate(user);

        assertFalse(violations.isEmpty());
        violations.clear();

        user = User.builder()
                .email("dzeensibir@yandex.ru")
                .login("   ")
                .name("Peter")
                .birthday(LocalDate.of(1991, 3, 3))
                .build();
        violations = validator.validate(user);

        assertFalse(violations.isEmpty());
        violations.clear();

        user = User.builder()
                .email("dzeensibir@yandex.ru")
                .login("peterPopov")
                .name("Peter")
                .birthday(LocalDate.of(1991, 3, 3))
                .build();
        violations = validator.validate(user);

        assertTrue(violations.isEmpty());
        violations.clear();
    }

    @Test
    public void invalidBirthday() {
        user = User.builder()
                .email("dzeensibir@yandex.ru")
                .login("peterPopov")
                .name("Peter")
                .birthday(LocalDate.of(2022, 12, 31))
                .build();
        violations = validator.validate(user);

        assertFalse(violations.isEmpty());
        violations.clear();
    }

}
