package com.task.gymproject.service.api;

import com.task.gymproject.model.Training;

import java.util.UUID;

public interface TrainingService {

    void createTraining(final Training training);

    Training getTrainingById(final UUID id);

}
