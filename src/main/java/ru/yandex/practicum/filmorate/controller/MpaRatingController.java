package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
<<<<<<< HEAD
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
=======
import org.springframework.web.bind.annotation.*;
>>>>>>> 10448c9be0cb0a5825a0fa45bc444ab72bbe323e
import ru.yandex.practicum.filmorate.model.MpaRating;
import ru.yandex.practicum.filmorate.storage.MpaRatingStorage;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/mpa")
@RequiredArgsConstructor
@Slf4j
public class MpaRatingController {

    private final MpaRatingStorage mpaRatingStorage;

    @GetMapping
    public List<MpaRating> getAllMpaRatings() {
        log.info("Получен запрос на получение всех рейтингов MPA");
        return mpaRatingStorage.getAllMpaRatings();
    }

    @GetMapping("/{id}")
    public MpaRating getMpaRatingById(@PathVariable int id) {
        log.info("Получен запрос на получение рейтинга MPA с id: {}", id);
        Optional<MpaRating> rating = mpaRatingStorage.getMpaRatingById(id);
        if (rating.isEmpty()) {
<<<<<<< HEAD
            throw new ru.yandex.practicum.filmorate.exception.NotFoundException(
                    "Рейтинг MPA с id " + id + " не найден");
        }
        return rating.get();
    }
}
=======
            throw new ru.yandex.practicum.filmorate.exception.NotFoundException("Рейтинг MPA с id " + id + " не найден");
        }
        return rating.get();
    }
}
>>>>>>> 10448c9be0cb0a5825a0fa45bc444ab72bbe323e
