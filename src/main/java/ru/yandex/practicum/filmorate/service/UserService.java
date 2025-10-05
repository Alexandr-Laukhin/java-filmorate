package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FriendshipStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {
    @Qualifier("userDbStorage")
    private final UserStorage userStorage;
    private final FriendshipStorage friendshipStorage;

    public List<User> getAllUsers() {
        return userStorage.getAllUsers();
    }

    public User createUser(User user) {
        validateUser(user);
        return userStorage.createUser(user);
    }

    public User updateUser(User user) {
        validateUser(user);
        return userStorage.updateUser(user);
    }

    public User getUserById(int id) {
        return userStorage.getUserById(id);
    }

    public void deleteUser(int id) {
        userStorage.deleteUser(id);
    }

    public void addFriend(int userId, int friendId) {
        if (userId == friendId) {
            log.warn("Пользователь {} пытается добавить себя в друзья", userId);
            throw new ValidationException("Пользователь не может добавить себя в друзья");
        }

        userStorage.getUserById(userId);
        userStorage.getUserById(friendId);

        friendshipStorage.addFriend(userId, friendId);
    }

    public void removeFriend(int userId, int friendId) {
        userStorage.getUserById(userId);
        userStorage.getUserById(friendId);

        friendshipStorage.removeFriend(userId, friendId);
    }

    public List<User> getFriends(int userId) {
        userStorage.getUserById(userId);
        List<User> friends = friendshipStorage.getFriends(userId);
        
        log.info("Получен список друзей пользователя {}. Количество: {}", userId, friends.size());
        return friends;
    }

    public List<User> getCommonFriends(int userId, int otherId) {
        userStorage.getUserById(userId);
        userStorage.getUserById(otherId);

        List<User> commonFriends = friendshipStorage.getCommonFriends(userId, otherId);

        log.info("Получен список общих друзей пользователей {} и {}. Количество: {}",
                userId, otherId, commonFriends.size());
        return commonFriends;
    }

    private void validateUser(User user) {
        if (user.getLogin().contains(" ")) {
            log.warn("Логин пользователя содержит пробелы: {}", user.getLogin());
            throw new ValidationException("Логин не может содержать пробелы");
        }
    }
}