package ru.yandex.practicum.filmorate.storage.film;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.validator.FilmValidator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/films")
@Slf4j
@Component
public class InMemoryFilmStorage implements FilmStorage {
    @Getter
    private final Map<Integer, Film> films = new HashMap<>();
    private int nextId = 1;

    @Override
    public Film addFilm(Film film) {
        FilmValidator.validate(film);
        film.setId(nextId);
        films.put(nextId++, film);
        return film;
    }

    @Override
    public Film updateFilm(Film film) {
        FilmValidator.validate(film);
        int id = film.getId();

        if (films.containsKey(id)) {
            films.put(id, film);
            return film;
        }
        return null;
    }

    @Override
    public List<Film> getAllFilms() {
        return new ArrayList<>(films.values());
    }

    @Override
    public Film getFilm(int id) {
        return films.getOrDefault(id, null);
    }
}
