package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Primary
@Slf4j
public class InMemoryUserStorage implements UserStorage {
    private final Map<Integer, User> users = new HashMap<>();
    private int nextId = 1;

    @Override
    public List<User> getAllUsers() {
        List<User> result = new ArrayList<>(users.values());
        log.info("Получен запрос на получение всех пользователей. Количество: {}", result.size());
        return result;
    }

    @Override
    public User createUser(User user) {
        user.setId(nextId++);
        applyDefaultName(user);
        users.put(user.getId(), user);
        log.info("Добавлен новый пользователь: {}", user);
        return user;
    }

    @Override
    public User updateUser(User user) {
        if (user.getId() == 0 || !users.containsKey(user.getId())) {
            log.warn("Попытка обновления несуществующего пользователя с id: {}", user.getId());
            throw new NotFoundException("Пользователь с id " + user.getId() + " не найден");
        }
        applyDefaultName(user);
        users.put(user.getId(), user);
        log.info("Обновлен пользователь: {}", user);
        return user;
    }

    @Override
    public User getUserById(int id) {
        if (!users.containsKey(id)) {
            log.warn("Пользователь с id {} не найден", id);
            throw new NotFoundException("Пользователь с id " + id + " не найден");
        }
        return users.get(id);
    }

    @Override
    public void deleteUser(int id) {
        if (!users.containsKey(id)) {
            log.warn("Попытка удаления несуществующего пользователя с id: {}", id);
            throw new NotFoundException("Пользователь с id " + id + " не найден");
        }
        users.remove(id);
        log.info("Удален пользователь с id: {}", id);
    }

    private void applyDefaultName(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
    }
}
