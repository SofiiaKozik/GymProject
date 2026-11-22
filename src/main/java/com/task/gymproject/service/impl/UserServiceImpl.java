package com.task.gymproject.service.impl;

import com.task.gymproject.generator.PasswordGenerator;
import com.task.gymproject.model.User;
import com.task.gymproject.repository.api.TraineeDao;
import com.task.gymproject.repository.api.TrainerDao;
import com.task.gymproject.service.api.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private PasswordGenerator passwordGenerator;

    private final TraineeDao traineeDAO;

    private final TrainerDao trainerDAO;

    @Override
    public void initializeUser(final User user,
                               final String firstName,
                               final String lastName) {
        user.setId(UUID.randomUUID());
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setUsername(generateUsername(firstName, lastName));
        user.setPassword(passwordGenerator.generate());
        user.setActive(true);
    }

    private String generateUsername(final String firstName, final String lastName) {
        final var base = firstName + "." + lastName;
        if (!isUsernameTaken(base)) {
            return base;
        }
        var counter = 1;
        while (isUsernameTaken(base + counter)) {
            counter++;
        }

        return base + counter;
    }

    boolean isUsernameTaken(final String username) {
        return traineeDAO.existsByUsername(username)
                || trainerDAO.existsByUsername(username);
    }
}
