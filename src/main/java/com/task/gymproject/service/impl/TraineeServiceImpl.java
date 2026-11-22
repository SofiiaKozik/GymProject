package com.task.gymproject.service.impl;

import com.task.gymproject.exeption.EntityNotFoundExeption;
import com.task.gymproject.model.Trainee;
import com.task.gymproject.repository.api.TraineeDao;
import com.task.gymproject.service.api.TraineeService;
import com.task.gymproject.service.api.UserService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.UUID;

@Service
@AllArgsConstructor
public class TraineeServiceImpl implements TraineeService {
    private static final Logger logger =
            LoggerFactory.getLogger(TraineeServiceImpl.class);

    private final TraineeDao traineeDAO;
    private final UserService userService;

    @Override
    public void createTrainee(final String firstName,
                              final String lastName,
                              final LocalDate dateOfBirth,
                              final String address) {
        logger.info("Creating trainee: FirstName ={}, LastName={}", firstName, lastName);
        final var trainee = new Trainee();
        userService.initializeUser(trainee, firstName, lastName);
        trainee.setDateOfBirth(dateOfBirth);
        trainee.setAddress(address);
        traineeDAO.save(trainee);
        logger.info("Trainee successfully created");
    }

    @Override
    public Trainee getTraineeById(final UUID id) {
        logger.info("Searching for trainee with ID: {}", id);

        return traineeDAO.findById(id).orElseThrow(() -> {
                    logger.warn("Trainee with ID {} was not found", id);
                    throw new EntityNotFoundExeption("Trainee with ID " + id + " was not found");
                });
    }

    @Override
    public Trainee updateTrainee(final Trainee trainee) {
        logger.info("Updating trainee with ID: {}", trainee.getId());
        if (trainee.getId() == null) {
            throw new IllegalArgumentException("Trainee ID cannot be null for update.");
        }
        getTraineeById(trainee.getId());
        final var updatedTrainee = traineeDAO.update(trainee);
        logger.info("Trainee successfully updated");

        return updatedTrainee;
    }

    @Override
    public void deleteTrainee(final UUID id) {
        getTraineeById(id);
        traineeDAO.delete(id);
        logger.info("Trainee successfully deleted");
    }
}
