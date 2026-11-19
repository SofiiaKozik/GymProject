package com.task.gymproject.generator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class PasswordGeneratorTest {

    private PasswordGenerator passwordGenerator;

    @BeforeEach
    void setUp() {
        passwordGenerator = new PasswordGenerator();
    }

    @Test
    void generate_ShouldReturnPasswordWithExactly10Characters() {
        final var password = passwordGenerator.generate();

        assertEquals(10, password.length());
    }


    @Test
    void generate_ShouldUseOnlyAllowedCharacters() {
        final var password = passwordGenerator.generate();

        assertTrue(password.matches("[A-Za-z0-9]{10}"));
    }

    @Test
    void generate_ShouldReturnNonNullPassword() {
        final var password = passwordGenerator.generate();

        assertNotNull(password);
    }

    @Test
    void generate_ShouldReturnDifferentPasswordsForMultipleCalls() {
        final var firstPassword = passwordGenerator.generate();
        final var secondPassword = passwordGenerator.generate();

        assertNotEquals(firstPassword, secondPassword);
    }

    @Test
    void constructor_ShouldInitializeGeneratorCorrectly() {
        final var generator = new PasswordGenerator();

        assertNotNull(generator);
    }
}
