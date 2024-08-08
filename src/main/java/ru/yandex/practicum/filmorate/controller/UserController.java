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
    private int nextId = 1;

    @PostMapping
    public ResponseEntity<User> addUser(@RequestBody User user) {
        try {
            UserValidator.validate(user);
            user.setId(nextId++);
            users.add(user);
            log.info("Пользователь добавлен: " + user.getLogin());
            return new ResponseEntity<>(user, HttpStatus.CREATED);
        } catch (ValidationException e) {
            log.error("Ошибка валидации при добавлении пользователя: " + e.getMessage());
            return ResponseEntity.badRequest().body(user);
        }
    }

    @PutMapping
    public ResponseEntity<User> updateUser(@RequestBody User user) {
        if (user == null) {
            log.error("Фильм не может быть null");
            return ResponseEntity.badRequest().build();
        }

        try {
            UserValidator.validate(user);
            int id = user.getId();
            int index = -1;

            for (int i = 0; i < users.size(); i++) {
                if (users.get(i).getId() == id) {
                    index = i;
                    break;
                }
            }

            if (index != -1) {
                users.set(index, user);
                log.info("Фильм обновлён: " + user.getName());
                return ResponseEntity.ok(user);
            } else {
                log.info("Индекс не найден");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(user);
            }
        } catch (ValidationException e) {
            log.error("Ошибка валидации при обновлении фильма: " + e.getMessage());
            return ResponseEntity.badRequest().body(user);
        }
    }

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(users);
    }
}
