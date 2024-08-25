package ru.yandex.practicum.filmorate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
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
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<User> addUser(@RequestBody User user) {
        User createdUser = userService.addUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }

    @PutMapping
    public ResponseEntity<User> updateUser(@RequestBody User user) {
        if (user == null) {
            log.error("Фильм не может быть null");
            return ResponseEntity.badRequest().build();
        }

        UserValidator.validate(user);
        int id = user.getId();

        if (userService.getUsers().containsKey(id)) {
            User updatedUser = userService.updateUser(user);
            log.info("Фильм обновлён: " + updatedUser.getName());
            return ResponseEntity.ok(updatedUser);
        } else {
            log.info("Индекс не найден");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(user);
        }
    }

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> usersList = new ArrayList<>(userService.getUsers().values());
        return ResponseEntity.ok(usersList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUser(@PathVariable int id) {
        User user = userService.getUser(id);
        if (user == null) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok().body(user);
    }

    @PutMapping("/{userId}/friends/{friendId}")
    public ResponseEntity<User> addFriend(@PathVariable int userId, @PathVariable int friendId) {
        boolean value = userService.addFriend(userId, friendId);
        if (value) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(userService.getUsers().get(userId));
        }
    }

    @DeleteMapping("/{userId}/friends/{friendId}")
    public ResponseEntity<Void> removeFriend(@PathVariable int userId, @PathVariable int friendId) {
        userService.removeFriend(userId, friendId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}/friends")
    public ResponseEntity<List<User>> getFriends(@PathVariable int id) {
        List<User> friends = userService.getFriends(id);
        return ResponseEntity.ok().body(friends);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public ResponseEntity<List<User>> getCommonFriends(@PathVariable int id, @PathVariable int otherId) {
        List<User> commonFriends = userService.getCommonFriends(id, otherId);
        return ResponseEntity.ok().body(commonFriends);
    }
}
