package com.task.gymproject.generator;

import org.springframework.stereotype.Component;

import java.security.SecureRandom;

@Component
public class PasswordGenerator {

    private static final String CHARACTERS =
            "ABCDEFGHIJKLMNOPQRSTUVWXYZ" +
                    "abcdefghijklmnopqrstuvwxyz" +
                    "0123456789";

    private static final SecureRandom RANDOM = new SecureRandom();

    public String generate() {
        final var password = new StringBuilder();
        for (int i = 0; i < 10; i++) {
            final var index = RANDOM.nextInt(
                    CHARACTERS.length());

            password.append(CHARACTERS.charAt(index));
        }

        return password.toString();
    }
}
