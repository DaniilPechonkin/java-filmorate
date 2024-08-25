package ru.yandex.practicum.filmorate;

import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.film.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class FilmControllerTest {
    private FilmController filmController;

    @BeforeEach
    void setUp() {
        FilmStorage filmStorage = new InMemoryFilmStorage();
        UserStorage userStorage = new InMemoryUserStorage();
        FilmService filmService = new FilmService(userStorage, filmStorage);
        filmController = new FilmController(filmService);
    }

    @Test
    void addFilm_ValidFilm_ReturnsCreatedResponse() {
        Film film = new Film();
        film.setName("Valid Film");
        film.setDuration(10);
        film.setDescription("film");
        film.setReleaseDate(LocalDate.now());

        ResponseEntity<Film> response = filmController.addFilm(film);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Valid Film", response.getBody().getName());
    }

    @Test
    void addFilm_InvalidName_ReturnsBadRequest() {
        Film film = new Film();
        film.setName("");

        ValidationException exception = assertThrows(ValidationException.class, () -> {
            filmController.addFilm(film);
        });

        assertEquals("Название фильма не может быть пустым.", exception.getMessage());
    }

    @Test
    void addFilm_InvalidDescription_ReturnsBadRequest() {
        Film film = new Film();
        film.setName("Film");
        film.setDuration(10);
        film.setReleaseDate(LocalDate.now());
        film.setDescription("A".repeat(201));
        ValidationException exception = assertThrows(ValidationException.class, () -> {
            filmController.addFilm(film);
        });

        assertEquals("Максимальная длина описания - 200 символов.", exception.getMessage());
    }

    @Test
    void addFilm_InvalidReleaseDate_ReturnsBadRequest() {
        Film film = new Film();
        film.setName("Film");
        film.setDuration(10);
        film.setDescription("film");
        film.setReleaseDate(LocalDate.of(1895, 12, 27));

        ValidationException exception = assertThrows(ValidationException.class, () -> {
            filmController.addFilm(film);
        });

        assertEquals("Дата релиза не может быть раньше 28 декабря 1895 года.", exception.getMessage());
    }

    @Test
    void addFilm_InvalidDuration_ReturnsBadRequest() {
        Film film = new Film();
        film.setName("Film");
        film.setDescription("film");
        film.setReleaseDate(LocalDate.now());
        film.setDuration(0);

        ValidationException exception = assertThrows(ValidationException.class, () -> {
            filmController.addFilm(film);
        });

        assertEquals("Продолжительность фильма должна быть больше 0.", exception.getMessage());
    }

    @Test
    void updateFilm_ValidFilm_ReturnsOkResponse() {
        Film film = new Film();
        film.setName("First Film");
        film.setDuration(10);
        film.setDescription("film");
        film.setReleaseDate(LocalDate.now());
        filmController.addFilm(film);

        Film updatedFilm = new Film();
        updatedFilm.setId(film.getId());
        updatedFilm.setName("Updated Film");
        updatedFilm.setDuration(10);
        updatedFilm.setDescription("updFilm");
        updatedFilm.setReleaseDate(LocalDate.now());

        ResponseEntity<Film> response = filmController.updateFilm(updatedFilm);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Updated Film", response.getBody().getName());
    }

    @Test
    void updateFilm_NonExistentFilm_ReturnsNotFound() {
        Film updatedFilm = new Film();
        updatedFilm.setName("Updated Film");
        updatedFilm.setDuration(10);
        updatedFilm.setDescription("updFilm");
        updatedFilm.setReleaseDate(LocalDate.now());

        ResponseEntity<Film> response = filmController.updateFilm(updatedFilm);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void getAllFilms_ReturnsListOfFilms() {
        Film film1 = new Film();
        film1.setName("Film 1");
        film1.setDuration(10);
        film1.setDescription("film1");
        film1.setReleaseDate(LocalDate.now());
        filmController.addFilm(film1);

        Film film2 = new Film();
        film2.setName("Film 2");
        film2.setDuration(10);
        film2.setDescription("film2");
        film2.setReleaseDate(LocalDate.now());
        filmController.addFilm(film2);

        ResponseEntity<List<Film>> response = filmController.getAllFilms();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());
    }
}
