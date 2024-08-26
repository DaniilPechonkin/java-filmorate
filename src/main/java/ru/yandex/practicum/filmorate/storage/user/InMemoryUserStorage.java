package ru.yandex.practicum.filmorate.storage.user;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.ResourceNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.validator.UserValidator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class InMemoryUserStorage implements UserStorage {
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

        if (user != null && users.containsKey(id)) {
            users.put(id, user);
            return user;
        } else {
            throw new ResourceNotFoundException("Пользователь не существует");
        }
    }

    @Override
    public User getUser(int id) {
        User user = users.getOrDefault(id, null);
        if (user == null) {
            throw new ResourceNotFoundException("Пользователь с id " + id + " не найден");
        }
        return user;
    }

    @Override
    public List<User> getUsers() {
        return new ArrayList<>(users.values());
    }
}
