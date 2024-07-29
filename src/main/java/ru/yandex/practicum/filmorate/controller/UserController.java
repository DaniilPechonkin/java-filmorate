package ru.yandex.practicum.filmorate.controller;

import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.validator.UserValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {
    private final List<User> users = new ArrayList<>();
    @PostMapping
    public ResponseEntity<User> addUser(@RequestBody User user) {
        try {
            UserValidator.validate(user);
            users.add(user);
            log.info("Пользователь добавлен: " + user.getLogin());
            return new ResponseEntity<>(user, HttpStatus.CREATED);
        } catch (ValidationException e) {
            log.error("Ошибка валидации при добавлении пользователя: " + e.getMessage());
            return ResponseEntity.badRequest().body(null);
        }
    }

    @PutMapping("/{index}")
    public ResponseEntity<User> updateUser(@PathVariable int index, @RequestBody User user) {
        try {
            UserValidator.validate(user);
            if (index < 0 || index >= users.size()) {
                log.warn("Пользователь с индексом " + index + " не найден для обновления.");
                return ResponseEntity.notFound().build();
            }
            users.set(index, user);
            log.info("Пользователь обновлён: " + user.getLogin());
            return ResponseEntity.ok(user);
        } catch (ValidationException e) {
            log.error("Ошибка валидации при обновлении пользователя: " + e.getMessage());
            return ResponseEntity.badRequest().body(null);
        }
    }

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(users);
    }
}
