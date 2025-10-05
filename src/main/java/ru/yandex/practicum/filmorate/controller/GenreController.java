package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.GenreStorage;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/genres")
@RequiredArgsConstructor
@Slf4j
public class GenreController {
    private final GenreStorage genreStorage;

    @GetMapping
    public List<Genre> getAllGenres() {
        log.info("Получен запрос на получение всех жанров");
        return genreStorage.getAllGenres();
    }

    @GetMapping("/{id}")
    public Genre getGenreById(@PathVariable int id) {
        log.info("Получен запрос на получение жанра с id: {}", id);
        Optional<Genre> genre = genreStorage.getGenreById(id);
        if (genre.isEmpty()) {
            throw new ru.yandex.practicum.filmorate.exception.NotFoundException("Жанр с id " + id + " не найден");
        }
        return genre.get();
    }
}
