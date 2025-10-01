package ru.yandex.practicum.filmorate.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum FriendshipStatus {
    UNCONFIRMED("неподтверждённая"),
    CONFIRMED("подтверждённая");

    private final String description;
}
