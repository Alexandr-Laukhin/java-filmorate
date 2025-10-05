package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class LikeStorage {

    private final JdbcTemplate jdbcTemplate;

    public void addLike(int filmId, int userId) {
        String sql = "INSERT INTO likes (film_id, user_id) VALUES (?, ?)";
        jdbcTemplate.update(sql, filmId, userId);
        log.info("Пользователь {} поставил лайк фильму {}", userId, filmId);
    }

    public void removeLike(int filmId, int userId) {
        String sql = "DELETE FROM likes WHERE film_id = ? AND user_id = ?";
        int rowsDeleted = jdbcTemplate.update(sql, filmId, userId);
        
        if (rowsDeleted == 0) {
            log.info("Пользователь {} не ставил лайк фильму {}, операция пропущена", userId, filmId);
        } else {
            log.info("Пользователь {} убрал лайк с фильма {}", userId, filmId);
        }
    }
}
