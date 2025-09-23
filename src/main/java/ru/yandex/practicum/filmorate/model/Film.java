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
    // видел, что ты писал, что надо эти аннотации объединить в одну, чтобы это и для create подходило,
    // и для update, но не смог придумать, как это сделать. Затык в обновлении только по id. Если сделать так
    // как ты сказал, имя станет обязательным, и мы не сможем обновить, например, только id, не обновив имя.
    // Возможно я тебя не так понял? Подскажи, что имелось ввиду? В остальном вроде все поправил.

    @Size(max = 200, groups = {Create.class, Update.class}, message = "Максимальная длина описания — 200 символов")
    private String description;

    @NotNull(groups = Create.class, message = "Дата релиза обязательна")
    private LocalDate releaseDate;

    @Positive(groups = {Create.class, Update.class}, message = "Продолжительность фильма должна быть положительным числом")
    private Integer duration;
}