package ru.yandex.practicum.filmorate.model;

import lombok.Data;
<<<<<<< HEAD

@Data
public class Friendship {
    private Integer userId;
    private Integer friendId;
=======
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Friendship {
    private int userId;
    private int friendId;
>>>>>>> 10448c9be0cb0a5825a0fa45bc444ab72bbe323e
    private FriendshipStatus status;
}
