package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(of = "id")
public class User {
    int id;
    String email;
    String login;
    String name;
    LocalDateTime birthday;
}
