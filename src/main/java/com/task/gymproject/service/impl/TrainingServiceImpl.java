package com.task.gymproject.service.impl;

import com.task.gymproject.repository.api.TrainingDao;
import com.task.gymproject.exeption.EntityNotFoundExeption;
import com.task.gymproject.model.Training;
import com.task.gymproject.service.api.TrainingService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@AllArgsConstructor
public class TrainingServiceImpl implements TrainingService {

    private static final Logger logger =
            LoggerFactory.getLogger(TrainingServiceImpl.class);

    private final TrainingDao trainingDAO;

    @Override
    public void createTraining(final Training training) {
        logger.info("Creating training with ID: {}", training.getId());
        trainingDAO.save(training);
        logger.info("Training successfully created");
    }

    @Override
    public Training getTrainingById(final UUID id) {
        logger.info("Searching for training with ID: {}", id);
        return trainingDAO.findById(id).orElseThrow(() -> {
            logger.warn("Training with ID {} was not found", id);
            throw new EntityNotFoundExeption("Training with ID " + id + " was not found");
        });
    }
}
