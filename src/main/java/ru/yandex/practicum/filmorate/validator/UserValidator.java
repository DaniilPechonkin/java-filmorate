package ru.yandex.practicum.filmorate.validator;

import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDateTime;

public class UserValidator {
    public static void validate(User user) {
        if (user.getEmail().isEmpty() || user.getEmail() == null || !user.getEmail().contains("@")) {
            throw new ValidationException("Почта не должна быть пустой и должна содержать @.");
        }
        if (user.getLogin().isEmpty() || user.getLogin() == null || user.getLogin().contains(" ")) {
            throw new ValidationException("Логин не должен быть пустым и содержать пробелов.");
        }
        if (user.getName().isEmpty()) {
            user.setName(user.getLogin());
        }
        if (user.getBirthday() == null || user.getBirthday().isAfter(LocalDateTime.now())) {
            throw new ValidationException("Дата рождения не может быть в будущем.");
        }
    }
}
