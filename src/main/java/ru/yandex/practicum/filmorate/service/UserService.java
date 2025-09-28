package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@Slf4j
public class UserService {
    private final UserStorage userStorage;

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

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

    public User getUserById(Integer id) {
        return userStorage.getUserById(id);
    }

    public void deleteUser(Integer id) {
        userStorage.deleteUser(id);
    }

    public void addFriend(Integer userId, Integer friendId) {
        User user = userStorage.getUserById(userId);
        User friend = userStorage.getUserById(friendId);
        
        if (user.getFriends().contains(friendId)) {
            log.warn("Пользователь {} уже является другом пользователя {}", friendId, userId);
            throw new ValidationException("Пользователь уже является другом");
        }
        
        user.getFriends().add(friendId);
        friend.getFriends().add(userId);
        
        log.info("Пользователь {} добавлен в друзья к пользователю {}", friendId, userId);
    }

    public void removeFriend(Integer userId, Integer friendId) {
        User user = userStorage.getUserById(userId);
        User friend = userStorage.getUserById(friendId);
        
        if (!user.getFriends().contains(friendId)) {
            log.warn("Пользователь {} не является другом пользователя {}", friendId, userId);
            throw new ValidationException("Пользователь не является другом");
        }
        
        user.getFriends().remove(friendId);
        friend.getFriends().remove(userId);
        
        log.info("Пользователь {} удален из друзей пользователя {}", friendId, userId);
    }

    public List<User> getFriends(Integer userId) {
        User user = userStorage.getUserById(userId);
        List<User> friends = new ArrayList<>();
        
        for (Integer friendId : user.getFriends()) {
            friends.add(userStorage.getUserById(friendId));
        }
        
        log.info("Получен список друзей пользователя {}. Количество: {}", userId, friends.size());
        return friends;
    }

    public List<User> getCommonFriends(Integer userId, Integer otherId) {
        User user = userStorage.getUserById(userId);
        User other = userStorage.getUserById(otherId);
        
        Set<Integer> commonFriendIds = new HashSet<>(user.getFriends());
        commonFriendIds.retainAll(other.getFriends());
        
        List<User> commonFriends = new ArrayList<>();
        for (Integer friendId : commonFriendIds) {
            commonFriends.add(userStorage.getUserById(friendId));
        }
        
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
