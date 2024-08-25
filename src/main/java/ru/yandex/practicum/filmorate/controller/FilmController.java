package ru.yandex.practicum.filmorate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
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
    private final FilmService filmService;

    @Autowired
    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    @PostMapping
    public ResponseEntity<Film> addFilm(@RequestBody Film film) {
        Film createdFilm = filmService.addFilm(film);
        return new ResponseEntity<>(createdFilm, HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<Film> updateFilm(@RequestBody Film film) {
        if (film == null) {
            log.error("Фильм не может быть null");
            return ResponseEntity.badRequest().build();
        }

        FilmValidator.validate(film);
        int id = film.getId();

        if (filmService.getFilms().containsKey(id)) {
            Film updatedFilm = filmService.updateFilm(film);
            log.info("Фильм обновлён: " + updatedFilm.getName());
            return ResponseEntity.ok(updatedFilm);
        } else {
            log.info("Индекс не найден");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(film);
        }
    }

    @GetMapping
    public ResponseEntity<List<Film>> getAllFilms() {
        List<Film> filmsList = new ArrayList<>(filmService.getFilms().values());
        return ResponseEntity.ok(filmsList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Film> getFilm(@PathVariable int id) {
        Film film = filmService.getFilm(id);
        if (film == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(film);
        }
        return ResponseEntity.ok().body(film);
    }

    @PutMapping("/{filmId}/like/{userId}")
    public ResponseEntity<Film> addLike(@PathVariable int filmId, @PathVariable int userId) {
        boolean value = filmService.addLike(filmId, userId);
        if (value) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(filmService.getFilms().get(filmId));
        }
    }

    @DeleteMapping("/{filmId}/like/{userId}")
    public ResponseEntity<Film> removeLike(@PathVariable int filmId, @PathVariable int userId) {
        filmService.removeLike(filmId, userId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/popular")
    public ResponseEntity<List<Film>> getTopFilms(@RequestParam(defaultValue = "10") int count) {
        List<Film> topFilms = filmService.getTopFilms(count);
        return ResponseEntity.ok(topFilms);
    }
}