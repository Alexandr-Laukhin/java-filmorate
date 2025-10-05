package ru.yandex.practicum.filmorate.storage;

import java.util.ArrayList;
import java.util.List;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Friendship;
import ru.yandex.practicum.filmorate.model.FriendshipStatus;
import ru.yandex.practicum.filmorate.model.User;

@Slf4j
@Component
@Primary
public class InMemoryFriendshipStorage implements FriendshipStorageInterface {
    private final List<Friendship> friendships = new ArrayList<>();

    @Override
    public void addFriend(int userId, int friendId) {
        Friendship friendship = new Friendship();
        friendship.setUserId(userId);
        friendship.setFriendId(friendId);
        friendship.setStatus(FriendshipStatus.UNCONFIRMED);
        friendships.add(friendship);
        log.info("Пользователь {} добавлен в друзья к пользователю {}", friendId, userId);
    }

    @Override
    public void removeFriend(int userId, int friendId) {
        friendships.removeIf(f -> f.getUserId().equals(userId)
                && f.getFriendId().equals(friendId));
        log.info("Пользователь {} удален из друзей пользователя {}", friendId, userId);
    }

    @Override
    public List<User> getFriends(int userId) {
        List<User> friends = new ArrayList<>();
        for (Friendship friendship : friendships) {
            if (friendship.getUserId().equals(userId)) {
                User friend = new User();
                friend.setId(friendship.getFriendId());
                friends.add(friend);
            }
        }
        return friends;
    }

    @Override
    public List<User> getCommonFriends(int userId, int otherId) {
        List<User> commonFriends = new ArrayList<>();
        List<Integer> userFriends = getFriends(userId).stream()
                .mapToInt(User::getId)
                .boxed()
                .toList();
        List<Integer> otherFriends = getFriends(otherId).stream()
                .mapToInt(User::getId)
                .boxed()
                .toList();

        for (Integer friendId : userFriends) {
            if (otherFriends.contains(friendId)) {
                User friend = new User();
                friend.setId(friendId);
                commonFriends.add(friend);
            }
        }
        return commonFriends;
    }
}
