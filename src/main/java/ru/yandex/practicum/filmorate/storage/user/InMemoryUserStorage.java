package ru.yandex.practicum.filmorate.storage.user;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.validator.UserValidator;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/users")
@Slf4j
@Component
public class InMemoryUserStorage implements UserStorage {
    @Getter
    private final Map<Integer, User> users = new HashMap<>();
    private int nextId = 1;

    @Override
    public User addUser(User user) {
        UserValidator.validate(user);
        user.setId(nextId);
        users.put(nextId++,user);
        return user;
    }

    @Override
    public User updateUser(User user) {
        UserValidator.validate(user);
        int id = user.getId();

        if (users.containsKey(id)) {
            users.put(id, user);
            return user;
        }
        return null;
    }

    @Override
    public User getUser(int id) {
        return users.getOrDefault(id, null);
    }
}
