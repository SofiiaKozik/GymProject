package com.task.gymproject.service.api;

import com.task.gymproject.model.Trainer;
import com.task.gymproject.model.TrainingType;

import java.util.UUID;

public interface TrainerService {

    void createTrainer(final String firstName,
                       final String lastName,
                       final TrainingType specialization);

    Trainer getTrainerById(final UUID id);

    Trainer updateTrainer(final Trainer trainer);
}
