package ru.yandex.practicum.filmorate;

import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class UserControllerTest {

    private UserController userController;

    @BeforeEach
    void setUp() {
        userController = new UserController();
    }

    @Test
    void addUser_ValidUser_ReturnsCreatedResponse() {
        User user = new User();
        user.setLogin("validUser");
        user.setEmail("user@example.com");
        user.setName("example");
        user.setBirthday(LocalDate.now());

        ResponseEntity<User> response = userController.addUser(user);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("validUser", response.getBody().getLogin());
    }

    @Test
    void addUser_InvalidUser_ReturnsBadRequest() {
        User user = new User();
        user.setLogin("");
        user.setEmail("user@example.com");
        user.setName("example");
        user.setBirthday(LocalDate.now());

        ResponseEntity<User> response = userController.addUser(user);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void updateUser_ValidUser_ReturnsOkResponse() {
        User user = new User();
        user.setLogin("firstUser");
        user.setEmail("user@example.com");
        user.setName("example");
        user.setBirthday(LocalDate.now());
        userController.addUser(user);

        User updatedUser = new User();
        updatedUser.setId((user.getId()));
        updatedUser.setLogin("updatedUser");
        updatedUser.setEmail("updated@example.com");
        updatedUser.setName("updExample");
        updatedUser.setBirthday(LocalDate.now());

        ResponseEntity<User> response = userController.updateUser(updatedUser);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("updatedUser", response.getBody().getLogin());
    }

    @Test
    void updateUser_NonExistentUser_ReturnsNotFound() {
        User updatedUser = new User();
        updatedUser.setLogin("updatedUser");
        updatedUser.setEmail("user@example.com");
        updatedUser.setName("example");
        updatedUser.setBirthday(LocalDate.now());

        ResponseEntity<User> response = userController.updateUser(updatedUser);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void getAllUsers_ReturnsListOfUsers() {
        User user1 = new User();
        user1.setLogin("user1");
        user1.setEmail("user1@example.com");
        user1.setName("example1");
        user1.setBirthday(LocalDate.now());
        userController.addUser(user1);

        User user2 = new User();
        user2.setLogin("user2");
        user2.setEmail("user2@example.com");
        user2.setName("example2");
        user2.setBirthday(LocalDate.now());
        userController.addUser(user2);

        ResponseEntity<List<User>> response = userController.getAllUsers();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());
    }
}