package com.task.gymproject.facade;

import com.task.gymproject.model.*;
import com.task.gymproject.service.api.TraineeService;
import com.task.gymproject.service.api.TrainerService;
import com.task.gymproject.service.api.TrainingService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.UUID;

@Service
@AllArgsConstructor
public class GymFacade {
    private final TraineeService traineeService;
    private final TrainerService trainerService;
    private final TrainingService trainingService;

    public void createTrainee(final String firstName,
                              final String lastName,
                              final LocalDate dateOfBirth,
                              final String address) {
        traineeService.createTrainee(firstName, lastName, dateOfBirth, address);
    }

    public Trainee getTraineeById(final UUID id) {
        return traineeService.getTraineeById(id);
    }

    public void deleteTrainee(final UUID id) {
        traineeService.deleteTrainee(id);
    }

    public Trainee updateTrainee(final Trainee trainee) {
        return traineeService.updateTrainee(trainee);
    }

    public void createTrainer(final String firstName,
                              final String lastName,
                              final TrainingType specialization) {
        trainerService.createTrainer(firstName, lastName, specialization);
    }

    public Trainer getTrainerById(final UUID id) {
        return trainerService.getTrainerById(id);
    }

    public Trainer updateTrainer(final Trainer trainer) {
        return trainerService.updateTrainer(trainer);
    }

    public void createTraining(final Training training) {
        trainingService.createTraining(training);
    }

    public Training getTrainingById(final UUID id) {
        return trainingService.getTrainingById(id);
    }
}
