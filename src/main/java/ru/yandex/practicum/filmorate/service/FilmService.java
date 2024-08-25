package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ResourceNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;
import ru.yandex.practicum.filmorate.validator.FilmValidator;
import ru.yandex.practicum.filmorate.validator.UserValidator;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class FilmService {
    FilmStorage filmStorage;
    UserStorage userStorage;

    @Autowired
    public FilmService(UserStorage userStorage, FilmStorage filmStorage) {
        this.userStorage = userStorage;
        this.filmStorage = filmStorage;
    }

    public Film getFilm(int id) {
        return filmStorage.getFilm(id);
    }

    public Map<Integer, Film> getFilms() {
        return filmStorage.getFilms();
    }

    public Film addFilm(Film film) {
        return filmStorage.addFilm(film);
    }

    public Film updateFilm(Film film) {
        return filmStorage.updateFilm(film);
    }

    public boolean addLike(int filmId, int userId) {
        Film film = filmStorage.getFilms().get(filmId);
        User user = userStorage.getUsers().get(userId);
        if (user == null || film == null) {
            throw new ResourceNotFoundException("Пользователь или фильм не найден " + (user == null ? userId : filmId));
        }
        UserValidator.validate(user);
        FilmValidator.validate(film);

        if (!film.getLikes().contains(userId)) {
            film.getLikes().add(userId);
            return true;
        }
        return false;
    }

    public void removeLike(int filmId, int userId) {
        Film film = filmStorage.getFilms().get(filmId);
        User user = userStorage.getUsers().get(userId);
        if (user == null || film == null) {
            throw new ResourceNotFoundException("Пользователь или фильм не найден " + (user == null ? userId : filmId));
        }
        UserValidator.validate(user);
        FilmValidator.validate(film);

        film.getLikes().remove(userId);
    }

    public List<Film> getTopFilms(int count) {
        return filmStorage.getFilms().values().stream()
                .sorted((f1, f2) -> Integer.compare(f2.getLikes().size(), f1.getLikes().size()))
                .limit(count)
                .collect(Collectors.toList());
    }
}
