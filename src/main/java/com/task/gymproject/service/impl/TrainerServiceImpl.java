package com.task.gymproject.service.impl;

import com.task.gymproject.exeption.EntityNotFoundExeption;
import com.task.gymproject.model.Trainer;
import com.task.gymproject.model.TrainingType;
import com.task.gymproject.repository.api.TrainerDao;
import com.task.gymproject.service.api.TrainerService;
import com.task.gymproject.service.api.UserService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@AllArgsConstructor
public class TrainerServiceImpl implements TrainerService {

    private static final Logger logger =
            LoggerFactory.getLogger(TrainerServiceImpl.class);

    private final TrainerDao trainerDAO;
    private final UserService userService;

    @Override
    public void createTrainer(final String firstName,
                              final String lastName,
                              final TrainingType specialization) {
        logger.info("Creating trainer: FirstName ={}, LastName={}", firstName, lastName);
        final var trainer = new Trainer();
        userService.initializeUser(trainer, firstName, lastName);
        trainer.setSpecialization(specialization);
        trainerDAO.save(trainer);
        logger.info("Trainer successfully created");
    }

    @Override
    public Trainer getTrainerById(final UUID id) {
        logger.info("Searching for trainer with ID: {}", id);

        return trainerDAO.findById(id).orElseThrow(() -> {
            logger.warn("Trainer with ID {} was not found", id);
            throw new EntityNotFoundExeption("Trainer with ID " + id + " was not found");
        });
    }

    @Override
    public Trainer updateTrainer(final Trainer trainer) {
        logger.info("Updating trainer with ID: {}", trainer.getId());
        if (trainer.getId() == null) {
            throw new IllegalArgumentException("Trainer ID cannot be null for update.");
        }
        getTrainerById(trainer.getId());
        final var updatedTrainer = trainerDAO.update(trainer);
        logger.info("Trainer successfully updated");

        return updatedTrainer;
    }
}
