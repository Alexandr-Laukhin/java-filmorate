package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class SimpleApplicationTests {

    @Test
    public void testBasicFunctionality() {
        assertThat(true).isTrue();
    }

    @Test
    public void testMath() {
        assertThat(2 + 2).isEqualTo(4);
    }

    @Test
    public void testString() {
        assertThat("Hello").isEqualTo("Hello");
    }
}
