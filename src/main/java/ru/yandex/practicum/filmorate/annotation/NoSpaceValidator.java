package ru.yandex.practicum.filmorate.annotation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class NoSpaceValidator implements ConstraintValidator<NoSpace, String> {

    @Override
    public void initialize(NoSpace constraintAnnotation) {
    }

    @Override
    public boolean isValid(String login, ConstraintValidatorContext context) {
        boolean result = true;
        if (login != null) {
            result = !login.contains(" ");
        }
        return result;
    }
}