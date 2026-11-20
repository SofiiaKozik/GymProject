package com.task.gymproject.service.api;

import com.task.gymproject.model.Trainee;

import java.time.LocalDate;
import java.util.UUID;

public interface TraineeService {

    void createTrainee(final String firstName,
                       final String lastName,
                       final LocalDate dateOfBirth,
                       final String address);

    Trainee getTraineeById(final UUID id);

    void deleteTrainee(final UUID id);

    Trainee updateTrainee(final Trainee trainee);
}
