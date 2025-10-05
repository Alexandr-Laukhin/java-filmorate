package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Slf4j
@Component
@Primary
public class InMemoryLikeStorage implements LikeStorageInterface {
    private final Set<String> likes = new HashSet<>();

    @Override
    public void addLike(int filmId, int userId) {
        likes.add(filmId + ":" + userId);
        log.info("Пользователь {} поставил лайк фильму {}", userId, filmId);
    }

    @Override
    public void removeLike(int filmId, int userId) {
        likes.remove(filmId + ":" + userId);
        log.info("Пользователь {} убрал лайк с фильма {}", userId, filmId);
    }
}
