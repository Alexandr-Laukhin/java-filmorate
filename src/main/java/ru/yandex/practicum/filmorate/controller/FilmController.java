package ru.yandex.practicum.filmorate.controller;

// removed unused imports
import org.springframework.validation.annotation.Validated;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {
    private final Map<Integer, Film> films = new HashMap<>();
    private int nextId = 1;
    private static final LocalDate MIN_RELEASE_DATE = LocalDate.of(1895, 12, 28);

    @GetMapping
    public List<Film> getAllFilms() {
        List<Film> result = new ArrayList<>(films.values());
        log.info("Получен запрос на получение всех фильмов. Количество: {}", result.size());
        return result;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Film createFilm(@Validated(ru.yandex.practicum.filmorate.model.validation.Create.class) @RequestBody Film film) {
        validateFilm(film);
        film.setId(nextId++);
        films.put(film.getId(), film);
        log.info("Добавлен новый фильм: {}", film);
        return film;
    }

    @PutMapping
    public Film updateFilm(@Validated(ru.yandex.practicum.filmorate.model.validation.Update.class) @RequestBody Film film) {
        if (film.getId() == null || !films.containsKey(film.getId())) {
            log.warn("Попытка обновления несуществующего фильма с id: {}", film.getId());
            throw new NotFoundException("Фильм с id " + film.getId() + " не найден");
        }
        Film stored = films.get(film.getId());
        if (film.getName() != null && !film.getName().isBlank()) {
            stored.setName(film.getName());
        }
        if (film.getDescription() != null) {
            stored.setDescription(film.getDescription());
        }
        if (film.getReleaseDate() != null) {
            stored.setReleaseDate(film.getReleaseDate());
        }
        if (film.getDuration() != null) {
            stored.setDuration(film.getDuration());
        }
        validateFilm(stored);
        films.put(stored.getId(), stored);
        log.info("Обновлен фильм: {}", stored);
        return stored;
    }

    private void validateFilm(Film film) {
        if (film.getReleaseDate().isBefore(MIN_RELEASE_DATE)) {
            log.warn("Дата релиза {} раньше минимальной допустимой {}", film.getReleaseDate(), MIN_RELEASE_DATE);
            throw new ValidationException("Дата релиза не может быть раньше 28 декабря 1895 года");
        }
    }
}
