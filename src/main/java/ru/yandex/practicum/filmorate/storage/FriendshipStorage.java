package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
@ConditionalOnProperty(name = "spring.datasource.url")
public class FriendshipStorage implements FriendshipStorageInterface {

    private final JdbcTemplate jdbcTemplate;

    public void addFriend(int userId, int friendId) {
        String sql = "INSERT INTO friendships (user_id, friend_id, status) VALUES (?, ?, 'UNCONFIRMED')";
        jdbcTemplate.update(sql, userId, friendId);
        log.info("Пользователь {} добавлен в друзья к пользователю {}", friendId, userId);
    }

    public void removeFriend(int userId, int friendId) {
        String sql = "DELETE FROM friendships WHERE user_id = ? AND friend_id = ?";
        int rowsDeleted = jdbcTemplate.update(sql, userId, friendId);

        if (rowsDeleted == 0) {
            log.info("Пользователь {} не является другом пользователя {}, операция пропущена", friendId, userId);
        } else {
            log.info("Пользователь {} удален из друзей пользователя {}", friendId, userId);
        }
    }

    public List<User> getFriends(int userId) {
        String sql = "SELECT u.id, u.email, u.login, u.name, u.birthday " +
                    "FROM users u " +
                    "JOIN friendships f ON u.id = f.friend_id " +
                    "WHERE f.user_id = ?";

        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            User user = new User();
            user.setId(rs.getInt("id"));
            user.setEmail(rs.getString("email"));
            user.setLogin(rs.getString("login"));
            user.setName(rs.getString("name"));
            if (rs.getDate("birthday") != null) {
                user.setBirthday(rs.getDate("birthday").toLocalDate());
            }
            return user;
        }, userId);
    }

    public List<User> getCommonFriends(int userId, int otherId) {
        String sql = "SELECT u.id, u.email, u.login, u.name, u.birthday " +
                    "FROM users u " +
                    "JOIN friendships f1 ON u.id = f1.friend_id " +
                    "JOIN friendships f2 ON u.id = f2.friend_id " +
                    "WHERE f1.user_id = ? AND f2.user_id = ?";

        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            User user = new User();
            user.setId(rs.getInt("id"));
            user.setEmail(rs.getString("email"));
            user.setLogin(rs.getString("login"));
            user.setName(rs.getString("name"));
            if (rs.getDate("birthday") != null) {
                user.setBirthday(rs.getDate("birthday").toLocalDate());
            }
            return user;
        }, userId, otherId);
    }
}