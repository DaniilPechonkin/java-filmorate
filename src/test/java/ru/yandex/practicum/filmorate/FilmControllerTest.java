package ru.yandex.practicum.filmorate;

import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.model.Film;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.Duration;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class FilmControllerTest {
    private FilmController filmController;

    @BeforeEach
    void setUp() {
        filmController = new FilmController();
    }

    @Test
    void addFilm_ValidFilm_ReturnsCreatedResponse() {
        Film film = new Film();
        film.setName("Valid Film");
        film.setDuration(Duration.ofHours(1));
        film.setDescription("film");
        film.setReleaseDate(LocalDate.now());


        ResponseEntity<Film> response = filmController.addFilm(film);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Valid Film", response.getBody().getName());
    }

    @Test
    void addFilm_InvalidFilm_ReturnsBadRequest() {
        Film film = new Film();
        film.setName("");

        ResponseEntity<Film> response = filmController.addFilm(film);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void updateFilm_ValidFilm_ReturnsOkResponse() {
        Film film = new Film();
        film.setName("First Film");
        film.setDuration(Duration.ofHours(1));
        film.setDescription("film");
        film.setReleaseDate(LocalDate.now());
        filmController.addFilm(film);

        Film updatedFilm = new Film();
        updatedFilm.setId(film.getId());
        updatedFilm.setName("Updated Film");
        updatedFilm.setDuration(Duration.ofHours(1));
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
        updatedFilm.setDuration(Duration.ofHours(1));
        updatedFilm.setDescription("updFilm");
        updatedFilm.setReleaseDate(LocalDate.now());

        ResponseEntity<Film> response = filmController.updateFilm(updatedFilm);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void getAllFilms_ReturnsListOfFilms() {
        Film film1 = new Film();
        film1.setName("Film 1");
        film1.setDuration(Duration.ofHours(1));
        film1.setDescription("film1");
        film1.setReleaseDate(LocalDate.now());
        filmController.addFilm(film1);

        Film film2 = new Film();
        film2.setName("Film 2");
        film2.setDuration(Duration.ofHours(1));
        film2.setDescription("film2");
        film2.setReleaseDate(LocalDate.now());
        filmController.addFilm(film2);

        ResponseEntity<List<Film>> response = filmController.getAllFilms();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());
    }
}
