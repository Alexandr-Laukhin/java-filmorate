package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserDbStorage;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Import({UserDbStorage.class})
class FilmorateApplicationTests {
    private final UserDbStorage userStorage;
    private User testUser;
    private User createdUser;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setEmail("test@example.com");
        testUser.setLogin("testuser");
        testUser.setName("Test User");
        testUser.setBirthday(LocalDate.of(1990, 1, 1));
        createdUser = userStorage.createUser(testUser);
    }

    @Test
    public void testFindUserById() {
        Optional<User> userOptional = userStorage.findUserById(createdUser.getId());
        assertThat(userOptional).isPresent()
                .hasValueSatisfying(foundUser ->
                        assertThat(foundUser).hasFieldOrPropertyWithValue("id", createdUser.getId())
                );
    }

    @Test
    public void testCreateUser() {
        assertThat(createdUser.getId()).isGreaterThan(0);
        assertThat(createdUser.getEmail()).isEqualTo("test@example.com");
        assertThat(createdUser.getLogin()).isEqualTo("testuser");
    }

    @Test
    public void testGetAllUsers() {
        List<User> users = userStorage.getAllUsers();
        assertThat(users).isNotEmpty();
    }

    @Test
    public void testUpdateUser() {
        User updateUser = new User();
        updateUser.setEmail("update@example.com");
        updateUser.setLogin("updateuser");
        updateUser.setName("Update User");
        updateUser.setBirthday(LocalDate.of(1990, 1, 1));

        User createdUpdateUser = userStorage.createUser(updateUser);
        createdUpdateUser.setName("Updated Name");

        User updatedUser = userStorage.updateUser(createdUpdateUser);
        assertThat(updatedUser.getName()).isEqualTo("Updated Name");
    }
}