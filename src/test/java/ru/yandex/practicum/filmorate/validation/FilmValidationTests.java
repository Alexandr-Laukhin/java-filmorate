package ru.yandex.practicum.filmorate.validation;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.validation.Create;
import ru.yandex.practicum.filmorate.model.validation.Update;

import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class FilmValidationTests {

    private Validator validator;
    private Film film;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
        film = new Film();
    }

    @Test
    void filmCreate_valid_ok() {
        film.setName("Test Film");
        film.setDescription("D".repeat(200));
        film.setReleaseDate(LocalDate.of(1895, 12, 28));
        film.setDuration(1);

        Set<ConstraintViolation<Film>> violations = validator.validate(film, Create.class);
        assertTrue(violations.isEmpty());
    }

    @Test
    void filmCreate_emptyName_violation() {
        film.setName(" ");
        film.setReleaseDate(LocalDate.of(2020, 1, 1));
        film.setDuration(10);

        Set<ConstraintViolation<Film>> violations = validator.validate(film, Create.class);
        assertFalse(violations.isEmpty());
        boolean hasNameMessage = violations.stream()
                .anyMatch(v -> "name".equals(v.getPropertyPath().toString())
                        && v.getMessage().contains("Название не может быть пустым"));
        assertTrue(hasNameMessage);
    }

    @Test
    void filmCreate_nullReleaseDate_violation() {
        film.setName("Name");
        film.setDuration(10);

        Set<ConstraintViolation<Film>> violations = validator.validate(film, Create.class);
        assertFalse(violations.isEmpty());
        boolean hasReleaseDateMessage = violations.stream()
                .anyMatch(v -> "releaseDate".equals(v.getPropertyPath().toString())
                        && v.getMessage().contains("Дата релиза обязательна"));
        assertTrue(hasReleaseDateMessage);
    }

    @Test
    void filmCreate_description201_violation() {
        film.setName("Name");
        film.setDescription("D".repeat(201));
        film.setReleaseDate(LocalDate.of(2020, 1, 1));
        film.setDuration(10);

        Set<ConstraintViolation<Film>> violations = validator.validate(film, Create.class);
        assertFalse(violations.isEmpty());
        boolean hasDescriptionMessage = violations.stream()
                .anyMatch(v -> "description".equals(v.getPropertyPath().toString())
                        && v.getMessage().contains("Максимальная длина описания"));
        assertTrue(hasDescriptionMessage);
    }

    @Test
    void filmCreate_durationZero_violation() {
        film.setName("Name");
        film.setReleaseDate(LocalDate.of(2020, 1, 1));
        film.setDuration(0);

        Set<ConstraintViolation<Film>> violations = validator.validate(film, Create.class);
        assertFalse(violations.isEmpty());
        boolean hasDurationMessage = violations.stream()
                .anyMatch(v -> "duration".equals(v.getPropertyPath().toString())
                        && v.getMessage().contains("Продолжительность фильма должна быть положительным числом"));
        assertTrue(hasDurationMessage);
    }

    @Test
    void filmUpdate_withoutId_violation() {
        film.setName("Name");

        Set<ConstraintViolation<Film>> violations = validator.validate(film, Update.class);
        assertFalse(violations.isEmpty());
        boolean hasIdMessage = violations.stream()
                .anyMatch(v -> "id".equals(v.getPropertyPath().toString())
                        && v.getMessage().contains("Id обязателен для обновления"));
        assertTrue(hasIdMessage);
    }

    @Test
    void filmUpdate_onlyId_ok() {
        film.setId(1);

        Set<ConstraintViolation<Film>> violations = validator.validate(film, Update.class);
        assertTrue(violations.isEmpty());
    }

    @Test
    void filmUpdate_blankName_violation() {
        film.setId(1);
        film.setName(" ");

        Set<ConstraintViolation<Film>> violations = validator.validate(film, Update.class);
        assertFalse(violations.isEmpty());
        boolean hasNameUpdateMessage = violations.stream()
                .anyMatch(v -> "name".equals(v.getPropertyPath().toString())
                        && v.getMessage().contains("Название не может быть пустым"));
        assertTrue(hasNameUpdateMessage);
    }

    @Test
    void filmUpdate_negativeDuration_violation() {
        film.setId(1);
        film.setDuration(-1);

        Set<ConstraintViolation<Film>> violations = validator.validate(film, Update.class);
        assertFalse(violations.isEmpty());
        boolean hasDurationMessage = violations.stream()
                .anyMatch(v -> "duration".equals(v.getPropertyPath().toString())
                        && v.getMessage().contains("Продолжительность фильма должна быть положительным числом"));
        assertTrue(hasDurationMessage);
    }
}



