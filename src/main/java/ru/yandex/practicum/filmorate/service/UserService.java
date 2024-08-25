package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ResourceNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;
import ru.yandex.practicum.filmorate.validator.UserValidator;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService {
    private final UserStorage userStorage;

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public User getUser(int id) {
        return userStorage.getUser(id);
    }

    public Map<Integer, User> getUsers() {
        return userStorage.getUsers();
    }

    public User addUser(User user) {
        return userStorage.addUser(user);
    }

    public User updateUser(User user) {
        return userStorage.updateUser(user);
    }

    public boolean addFriend(int userId, int friendId) {
        User user = userStorage.getUsers().get(userId);
        User friend = userStorage.getUsers().get(friendId);
        if (user == null || friend == null) {
            throw new ResourceNotFoundException("Пользователь не найден " + (user == null ? user : friend));
        }
        UserValidator.validate(user);
        UserValidator.validate(friend);

        if (!user.getFriends().contains(friendId) || !friend.getFriends().contains(userId)) {
            user.getFriends().add(friendId);
            friend.getFriends().add(userId);
            return true;
        }
        return false;
    }

    public void removeFriend(int userId, int friendId) {
        User user = userStorage.getUsers().get(userId);
        User friend = userStorage.getUsers().get(friendId);
        if (user == null || friend == null) {
            throw new ResourceNotFoundException("Пользователь не найден " + (user == null ? userId : friendId));
        }
        UserValidator.validate(user);
        UserValidator.validate(friend);
        user.getFriends().remove(friendId);
        friend.getFriends().remove(userId);
    }

    public List<User> getCommonFriends(int userId1, int userId2) {
        User user1 = userStorage.getUsers().get(userId1);
        User user2 = userStorage.getUsers().get(userId2);
        if (user1 == null || user2 == null) {
            throw new ResourceNotFoundException("Пользователь не найден " + (user1 == null ? user1 : user2));
        }
        UserValidator.validate(user1);
        UserValidator.validate(user2);

        Set<Integer> commonFriendIds = user1.getFriends()
                .stream()
                .filter(user2.getFriends()::contains)
                .collect(Collectors.toSet());

        return commonFriendIds.stream()
                .map(userStorage.getUsers()::get)
                .collect(Collectors.toList());
    }

    public List<User> getFriends(int userId) {
        User user = userStorage.getUser(userId);
        if (user == null) {
            throw new ResourceNotFoundException("Пользователь не найден " + userId);
        }
        UserValidator.validate(user);
        return user.getFriends().stream()
                .map(userStorage::getUser)
                .collect(Collectors.toList());
    }
}
