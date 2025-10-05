package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import ru.yandex.practicum.filmorate.model.validation.Create;
import ru.yandex.practicum.filmorate.model.validation.Update;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
public class User {
    @Min(value = 1, groups = Update.class, message = "Id должен быть больше 0")
    private int id;

    @NotBlank(groups = Create.class, message = "Электронная почта не может быть пустой")
    @Email(groups = {Create.class, Update.class}, message = "Электронная почта должна содержать символ @")
    private String email;

    @NotBlank(groups = Create.class, message = "Логин не может быть пустым")
    @Pattern(regexp = "\\S+", groups = {Create.class, Update.class}, message = "Логин не может содержать пробелы")
    private String login;

    private String name;

    @PastOrPresent(groups = {Create.class, Update.class}, message = "Дата рождения не может быть в будущем")
    private LocalDate birthday;

    private Set<Integer> friends = new HashSet<>();
    private Set<Friendship> friendships = new HashSet<>();
}