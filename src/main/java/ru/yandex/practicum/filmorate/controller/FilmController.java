package ru.yandex.practicum.filmorate.controller;

import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.validator.FilmValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {
    private final List<Film> films = new ArrayList<>();
    private int nextId = 1;

    @PostMapping
    public ResponseEntity<Film> addFilm(@RequestBody Film film) {
        try {
            FilmValidator.validate(film);
            film.setId(nextId++);
            films.add(film);
            log.info("Фильм добавлен: " + film.getName());
            return new ResponseEntity<>(film, HttpStatus.CREATED);
        } catch (ValidationException e) {
            log.error("Ошибка валидации при добавлении фильма: " + e.getMessage());
            return ResponseEntity.badRequest().body(film);
        }
    }

    @PutMapping
    public ResponseEntity<Film> updateFilm(@RequestBody Film film) {
        if (film == null) {
            log.error("Фильм не может быть null");
            return ResponseEntity.badRequest().build();
        }

        try {
            FilmValidator.validate(film);
            int id = film.getId();
            int index = -1;

            for (int i = 0; i < films.size(); i++) {
                if (films.get(i).getId() == id) {
                    index = i;
                    break;
                }
            }

            if (index != -1) {
                films.set(index, film);
                log.info("Фильм обновлён: " + film.getName());
                return ResponseEntity.ok(film);
            } else {
                log.info("Индекс не найден");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(film);
            }
        } catch (ValidationException e) {
            log.error("Ошибка валидации при обновлении фильма: " + e.getMessage());
            return ResponseEntity.badRequest().body(film);
        }
    }

    @GetMapping
    public ResponseEntity<List<Film>> getAllFilms() {
        return ResponseEntity.ok(films);
    }
}