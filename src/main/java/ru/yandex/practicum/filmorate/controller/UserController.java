package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import jakarta.validation.groups.Default;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {
    private final Map<Integer, User> users = new HashMap<>();
    private int nextId = 1;

    @GetMapping
    public List<User> getAllUsers() {
        List<User> result = new ArrayList<>(users.values());
        log.info("Получен запрос на получение всех пользователей. Количество: {}", result.size());
        return result;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public User createUser(@jakarta.validation.groups.ConvertGroup(from = Default.class, to = ru.yandex.practicum.filmorate.model.validation.Create.class) @Valid @RequestBody User user) {
        validateUser(user);
        user.setId(nextId++);
        applyDefaultName(user);
        users.put(user.getId(), user);
        log.info("Добавлен новый пользователь: {}", user);
        return user;
    }

    @PutMapping
    public User updateUser(@jakarta.validation.groups.ConvertGroup(from = Default.class, to = ru.yandex.practicum.filmorate.model.validation.Update.class) @Valid @RequestBody User user) {
        if (user.getId() == null || !users.containsKey(user.getId())) {
            log.warn("Попытка обновления несуществующего пользователя с id: {}", user.getId());
            throw new NotFoundException("Пользователь с id " + user.getId() + " не найден");
        }
        User stored = users.get(user.getId());
        if (user.getEmail() != null) {
            stored.setEmail(user.getEmail());
        }
        if (user.getLogin() != null) {
            stored.setLogin(user.getLogin());
        }
        if (user.getName() != null) {
            stored.setName(user.getName());
        }
        if (user.getBirthday() != null) {
            stored.setBirthday(user.getBirthday());
        }
        applyDefaultName(stored);
        validateUser(stored);
        users.put(stored.getId(), stored);
        log.info("Обновлен пользователь: {}", stored);
        return stored;
    }

    private void validateUser(User user) {
        if (user.getLogin().contains(" ")) {
            log.warn("Логин пользователя содержит пробелы: {}", user.getLogin());
            throw new ValidationException("Логин не может содержать пробелы");
        }
    }

    private void applyDefaultName(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
    }
}