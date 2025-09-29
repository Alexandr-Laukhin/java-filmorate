package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class InMemoryFilmStorage implements FilmStorage {
    private final Map<Integer, Film> films = new HashMap<>();
    private int nextId = 1;

    @Override
    public List<Film> getAllFilms() {
        List<Film> result = new ArrayList<>(films.values());
        log.info("Получен запрос на получение всех фильмов. Количество: {}", result.size());
        return result;
    }

    @Override
    public Film createFilm(Film film) {
        film.setId(nextId++);
        films.put(film.getId(), film);
        log.info("Добавлен новый фильм: {}", film);
        return film;
    }

    @Override
    public Film updateFilm(Film film) {
        if (film.getId() == null || !films.containsKey(film.getId())) {
            log.warn("Попытка обновления несуществующего фильма с id: {}", film.getId());
            throw new NotFoundException("Фильм с id " + film.getId() + " не найден");
        }
        films.put(film.getId(), film);
        log.info("Обновлен фильм: {}", film);
        return film;
    }

    @Override
    public Film getFilmById(int id) {
        if (!films.containsKey(id)) {
            log.warn("Фильм с id {} не найден", id);
            throw new NotFoundException("Фильм с id " + id + " не найден");
        }
        return films.get(id);
    }

    @Override
    public void deleteFilm(int id) {
        if (!films.containsKey(id)) {
            log.warn("Попытка удаления несуществующего фильма с id: {}", id);
            throw new NotFoundException("Фильм с id " + id + " не найден");
        }
        films.remove(id);
        log.info("Удален фильм с id: {}", id);
    }
}
