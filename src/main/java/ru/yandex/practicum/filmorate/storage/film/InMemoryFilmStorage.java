package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.ResourceNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.validator.FilmValidator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class InMemoryFilmStorage implements FilmStorage {
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

        if (film != null && films.containsKey(id)) {
            films.put(id, film);
            return film;
        } else {
            throw new ResourceNotFoundException("Фильм не существует");
        }
    }

    @Override
    public List<Film> getFilms() {
        return new ArrayList<>(films.values());
    }

    @Override
    public Film getFilm(int id) {
        Film film = films.getOrDefault(id, null);
        if (film == null) {
            throw new ResourceNotFoundException("Фильм с id " + id + " не найден");
        }
        return film;
    }
}
