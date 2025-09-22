package ru.yandex.practicum.filmorate;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.model.validation.Create;
import ru.yandex.practicum.filmorate.model.validation.Update;

import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class FilmorateApplicationTests {

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void filmCreate_valid_ok() {
        Film film = new Film();
        film.setName("Test Film");
        film.setDescription("D".repeat(200));
        film.setReleaseDate(LocalDate.of(1895, 12, 28));
        film.setDuration(1);

        Set<ConstraintViolation<Film>> violations = validator.validate(film, Create.class);
        assertTrue(violations.isEmpty());
    }

    @Test
    void filmCreate_emptyName_violation() {
        Film film = new Film();
        film.setName(" ");
        film.setReleaseDate(LocalDate.of(2020, 1, 1));
        film.setDuration(10);

        Set<ConstraintViolation<Film>> violations = validator.validate(film, Create.class);
        assertFalse(violations.isEmpty());
    }

    @Test
    void filmCreate_nullReleaseDate_violation() {
        Film film = new Film();
        film.setName("Name");
        film.setDuration(10);

        Set<ConstraintViolation<Film>> violations = validator.validate(film, Create.class);
        assertFalse(violations.isEmpty());
    }

    @Test
    void filmCreate_description201_violation() {
        Film film = new Film();
        film.setName("Name");
        film.setDescription("D".repeat(201));
        film.setReleaseDate(LocalDate.of(2020, 1, 1));
        film.setDuration(10);

        Set<ConstraintViolation<Film>> violations = validator.validate(film, Create.class);
        assertFalse(violations.isEmpty());
    }

    @Test
    void filmCreate_durationZero_violation() {
        Film film = new Film();
        film.setName("Name");
        film.setReleaseDate(LocalDate.of(2020, 1, 1));
        film.setDuration(0);

        Set<ConstraintViolation<Film>> violations = validator.validate(film, Create.class);
        assertFalse(violations.isEmpty());
    }

    @Test
    void filmUpdate_withoutId_violation() {
        Film film = new Film();
        film.setName("Name");

        Set<ConstraintViolation<Film>> violations = validator.validate(film, Update.class);
        assertFalse(violations.isEmpty());
    }

    @Test
    void filmUpdate_onlyId_ok() {
        Film film = new Film();
        film.setId(1);

        Set<ConstraintViolation<Film>> violations = validator.validate(film, Update.class);
        assertTrue(violations.isEmpty());
    }

    @Test
    void filmUpdate_blankName_violation() {
        Film film = new Film();
        film.setId(1);
        film.setName(" ");

        Set<ConstraintViolation<Film>> violations = validator.validate(film, Update.class);
        assertFalse(violations.isEmpty());
    }

    @Test
    void filmUpdate_negativeDuration_violation() {
        Film film = new Film();
        film.setId(1);
        film.setDuration(-1);

        Set<ConstraintViolation<Film>> violations = validator.validate(film, Update.class);
        assertFalse(violations.isEmpty());
    }

    @Test
    void userCreate_valid_ok() {
        User user = new User();
        user.setEmail("a@b.com");
        user.setLogin("login");
        user.setBirthday(LocalDate.now());

        Set<ConstraintViolation<User>> violations = validator.validate(user, Create.class);
        assertTrue(violations.isEmpty());
    }

    @Test
    void userCreate_blankEmail_violation() {
        User user = new User();
        user.setEmail(" ");
        user.setLogin("login");

        Set<ConstraintViolation<User>> violations = validator.validate(user, Create.class);
        assertFalse(violations.isEmpty());
    }

    @Test
    void userCreate_invalidEmail_violation() {
        User user = new User();
        user.setEmail("invalid");
        user.setLogin("login");

        Set<ConstraintViolation<User>> violations = validator.validate(user, Create.class);
        assertFalse(violations.isEmpty());
    }

    @Test
    void userCreate_blankLogin_violation() {
        User user = new User();
        user.setEmail("a@b.com");
        user.setLogin(" ");

        Set<ConstraintViolation<User>> violations = validator.validate(user, Create.class);
        assertFalse(violations.isEmpty());
    }

    @Test
    void userCreate_loginWithSpaces_violation() {
        User user = new User();
        user.setEmail("a@b.com");
        user.setLogin("lo gin");

        Set<ConstraintViolation<User>> violations = validator.validate(user, Create.class);
        assertFalse(violations.isEmpty());
    }

    @Test
    void userCreate_birthdayInFuture_violation() {
        User user = new User();
        user.setEmail("a@b.com");
        user.setLogin("login");
        user.setBirthday(LocalDate.now().plusDays(1));

        Set<ConstraintViolation<User>> violations = validator.validate(user, Create.class);
        assertFalse(violations.isEmpty());
    }

    @Test
    void userUpdate_withoutId_violation() {
        User user = new User();
        user.setEmail("a@b.com");

        Set<ConstraintViolation<User>> violations = validator.validate(user, Update.class);
        assertFalse(violations.isEmpty());
    }

    @Test
    void userUpdate_onlyId_ok() {
        User user = new User();
        user.setId(1);

        Set<ConstraintViolation<User>> violations = validator.validate(user, Update.class);
        assertTrue(violations.isEmpty());
    }

    @Test
    void userUpdate_invalidEmail_violation() {
        User user = new User();
        user.setId(1);
        user.setEmail("invalid");

        Set<ConstraintViolation<User>> violations = validator.validate(user, Update.class);
        assertFalse(violations.isEmpty());
    }

    @Test
    void userUpdate_loginWithSpaces_violation() {
        User user = new User();
        user.setId(1);
        user.setLogin("lo gin");

        Set<ConstraintViolation<User>> violations = validator.validate(user, Update.class);
        assertFalse(violations.isEmpty());
    }
}
