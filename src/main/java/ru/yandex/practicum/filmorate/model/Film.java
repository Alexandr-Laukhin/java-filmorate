package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.*;
import lombok.Data;
import ru.yandex.practicum.filmorate.model.validation.Create;
import ru.yandex.practicum.filmorate.model.validation.Update;

import java.time.LocalDate;

@Data
public class Film {
    @NotNull(groups = Update.class, message = "Id обязателен для обновления")
    private Integer id;

    @NotBlank(groups = Create.class, message = "Название не может быть пустым")
    @Pattern(regexp = ".*\\S.*", groups = Update.class, message = "Название не может быть пустым")
    private String name;

    @Size(max = 200, message = "Максимальная длина описания — 200 символов")
    private String description;

    @NotNull(groups = Create.class, message = "Дата релиза обязательна")
    private LocalDate releaseDate;

    @Positive(message = "Продолжительность фильма должна быть положительным числом")
    private Integer duration;
}