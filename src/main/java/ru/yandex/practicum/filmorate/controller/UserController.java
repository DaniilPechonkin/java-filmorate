package ru.yandex.practicum.filmorate.controller;

import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.validator.UserValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {
    private final Map<Integer,User> users = new HashMap<>();
    private int nextId = 1;

    @PostMapping
    public ResponseEntity<User> addUser(@RequestBody User user) {
        UserValidator.validate(user);
        user.setId(nextId);
        users.put(nextId++,user);
        log.info("Пользователь добавлен: " + user.getLogin());
        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<User> updateUser(@RequestBody User user) {
        if (user == null) {
            log.error("Фильм не может быть null");
            return ResponseEntity.badRequest().build();
        }

        UserValidator.validate(user);
        int id = user.getId();

        if (users.containsKey(id)) {
            users.put(id, user);
            log.info("Фильм обновлён: " + user.getName());
            return ResponseEntity.ok(user);
        } else {
            log.info("Индекс не найден");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(user);
        }
    }

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> usersList = new ArrayList<>(users.values());
        return ResponseEntity.ok(usersList);
    }
}
