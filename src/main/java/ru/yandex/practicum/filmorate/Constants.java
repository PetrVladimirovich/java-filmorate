package ru.yandex.practicum.filmorate;

import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import ru.yandex.practicum.filmorate.exception.ValidationException;

import java.util.ArrayList;
import java.util.List;
@Slf4j
public class Constants {

    private Constants() {}

    public static final String FILM_BIRTHDAY = "28.12.1895";

    public static void requestBodyValidationLogs(BindingResult bindingResult) {
        List<FieldError> errors = bindingResult.getFieldErrors();
        List<String> message = new ArrayList<>();
        for (FieldError e : errors) {
            message.add("@" + e.getField().toUpperCase() + " : " + e.getDefaultMessage() + "\n");
        }

        log.error("Недопустимые значения полей : " + message);
        throw new ValidationException("Недопустимые значения полей");
    }
}
