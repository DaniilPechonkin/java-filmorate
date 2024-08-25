package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Map;

public interface UserStorage {
    public User addUser(User user);

    public User updateUser(User user);

    public Map<Integer, User> getUsers();

    public User getUser(int id);
}
