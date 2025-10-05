package ru.yandex.practicum.filmorate.validation;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.model.validation.Create;
import ru.yandex.practicum.filmorate.model.validation.Update;

import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class UserValidationTests {

    private Validator validator;
    private User user;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
        user = new User();
        user.setEmail("test@example.com");
        user.setLogin("testuser");
        user.setBirthday(LocalDate.of(1990, 1, 1));
    }

    @Test
    void userCreate_valid_ok() {

        Set<ConstraintViolation<User>> violations = validator.validate(user, Create.class);
        assertTrue(violations.isEmpty());
    }

    @Test
    void userCreate_blankEmail_violation() {
        user.setEmail(" ");

        Set<ConstraintViolation<User>> violations = validator.validate(user, Create.class);
        assertFalse(violations.isEmpty());
        boolean hasEmailBlankMessage = violations.stream()
                .anyMatch(v -> "email".equals(v.getPropertyPath().toString())
                        && v.getMessage().contains("Электронная почта не может быть пустой"));
        assertTrue(hasEmailBlankMessage);
    }

    @Test
    void userCreate_invalidEmail_violation() {
        user.setEmail("invalid");

        Set<ConstraintViolation<User>> violations = validator.validate(user, Create.class);
        assertFalse(violations.isEmpty());
        boolean hasEmailInvalidMessage = violations.stream()
                .anyMatch(v -> "email".equals(v.getPropertyPath().toString())
                        && v.getMessage().contains("должна содержать символ @"));
        assertTrue(hasEmailInvalidMessage);
    }

    @Test
    void userCreate_blankLogin_violation() {
        user.setLogin(" ");

        Set<ConstraintViolation<User>> violations = validator.validate(user, Create.class);
        assertFalse(violations.isEmpty());
        boolean hasLoginBlankMessage = violations.stream()
                .anyMatch(v -> "login".equals(v.getPropertyPath().toString())
                        && v.getMessage().contains("Логин не может быть пустым"));
        assertTrue(hasLoginBlankMessage);
    }

    @Test
    void userCreate_loginWithSpaces_violation() {
        user.setLogin("lo gin");

        Set<ConstraintViolation<User>> violations = validator.validate(user, Create.class);
        assertFalse(violations.isEmpty());
        boolean hasLoginSpacesMessage = violations.stream()
                .anyMatch(v -> "login".equals(v.getPropertyPath().toString())
                        && v.getMessage().contains("Логин не может содержать пробелы"));
        assertTrue(hasLoginSpacesMessage);
    }

    @Test
    void userCreate_birthdayInFuture_violation() {
        user.setBirthday(LocalDate.now().plusDays(1));

        Set<ConstraintViolation<User>> violations = validator.validate(user, Create.class);
        assertFalse(violations.isEmpty());
        boolean hasBirthdayFutureMessage = violations.stream()
                .anyMatch(v -> "birthday".equals(v.getPropertyPath().toString())
                        && v.getMessage().contains("Дата рождения не может быть в будущем"));
        assertTrue(hasBirthdayFutureMessage);
    }

    @Test
    void userUpdate_withoutId_violation() {

        Set<ConstraintViolation<User>> violations = validator.validate(user, Update.class);
        assertFalse(violations.isEmpty());
        boolean hasIdUpdateMessage = violations.stream()
                .anyMatch(v -> "id".equals(v.getPropertyPath().toString())
                        && v.getMessage().contains("Id должен быть больше 0"));
        assertTrue(hasIdUpdateMessage);
    }

    @Test
    void userUpdate_onlyId_ok() {
        user.setId(1);

        Set<ConstraintViolation<User>> violations = validator.validate(user, Update.class);
        assertTrue(violations.isEmpty());
    }

    @Test
    void userUpdate_invalidEmail_violation() {
        user.setId(1);
        user.setEmail("invalid");

        Set<ConstraintViolation<User>> violations = validator.validate(user, Update.class);
        assertFalse(violations.isEmpty());
        boolean hasEmailInvalidOnUpdateMessage = violations.stream()
                .anyMatch(v -> "email".equals(v.getPropertyPath().toString())
                        && v.getMessage().contains("должна содержать символ @"));
        assertTrue(hasEmailInvalidOnUpdateMessage);
    }

    @Test
    void userUpdate_loginWithSpaces_violation() {
        user.setId(1);
        user.setLogin("lo gin");

        Set<ConstraintViolation<User>> violations = validator.validate(user, Update.class);
        assertFalse(violations.isEmpty());
        boolean hasLoginSpacesOnUpdateMessage = violations.stream()
                .anyMatch(v -> "login".equals(v.getPropertyPath().toString())
                        && v.getMessage().contains("Логин не может содержать пробелы"));
        assertTrue(hasLoginSpacesOnUpdateMessage);
    }
}