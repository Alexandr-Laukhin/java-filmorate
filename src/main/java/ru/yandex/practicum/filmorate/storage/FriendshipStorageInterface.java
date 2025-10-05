package ru.yandex.practicum.filmorate.storage;

import java.util.List;

import ru.yandex.practicum.filmorate.model.User;

public interface FriendshipStorageInterface {
    void addFriend(int userId, int friendId);

    void removeFriend(int userId, int friendId);

    List<User> getFriends(int userId);

    List<User> getCommonFriends(int userId, int otherId);
}
