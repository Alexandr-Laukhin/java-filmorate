package ru.yandex.practicum.filmorate.model;

<<<<<<< HEAD
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum FriendshipStatus {
    UNCONFIRMED("неподтверждённая"),
    CONFIRMED("подтверждённая");

    private final String description;
=======
public enum FriendshipStatus {
    UNCONFIRMED,
    CONFIRMED
>>>>>>> 10448c9be0cb0a5825a0fa45bc444ab72bbe323e
}
