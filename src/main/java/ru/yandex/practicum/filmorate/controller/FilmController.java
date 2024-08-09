package ru.yandex.practicum.filmorate.controller;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.validator.FilmValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {
    private final Map<Integer, Film> films = new HashMap<>();
    private int nextId = 1;

    @PostMapping
    public ResponseEntity<Film> addFilm(@RequestBody Film film) {
        FilmValidator.validate(film);

        film.setId(nextId);
        films.put(nextId++, film);
        log.info("Фильм добавлен: " + film.getName());
        return new ResponseEntity<>(film, HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<Film> updateFilm(@RequestBody Film film) {
        if (film == null) {
            log.error("Фильм не может быть null");
            return ResponseEntity.badRequest().build();
        }

        FilmValidator.validate(film);
        int id = film.getId();

        if (films.containsKey(id)) {
            films.put(id, film);
            log.info("Фильм обновлён: " + film.getName());
            return ResponseEntity.ok(film);
        } else {
            log.info("Индекс не найден");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(film);
        }
    }

    @GetMapping
    public ResponseEntity<List<Film>> getAllFilms() {
        List<Film> filmsList = new ArrayList<>(films.values());
        return ResponseEntity.ok(filmsList);
    }
}