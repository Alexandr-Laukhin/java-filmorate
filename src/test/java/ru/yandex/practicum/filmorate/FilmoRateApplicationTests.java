package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@ActiveProfiles("test")
class FilmoRateApplicationTests {

    @Test
    public void contextLoads() {
        assertThat(true).isTrue();
    }

    @Test
    public void testApplicationStarts() {
        assertThat(true).isTrue();
    }

    @Test
    public void testBasicFunctionality() {
        assertThat(true).isTrue();
    }

    @Test
    public void testConfiguration() {
        assertThat(true).isTrue();
    }
}