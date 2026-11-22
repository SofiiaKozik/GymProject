package com.task.gymproject.facade;

import com.task.gymproject.model.*;
import com.task.gymproject.service.api.TraineeService;
import com.task.gymproject.service.api.TrainerService;
import com.task.gymproject.service.api.TrainingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.time.LocalDate;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.*;

public class GymFacadeTest {
    @Mock
    private TraineeService traineeService;

    @Mock
    private TrainerService trainerService;

    @Mock
    private TrainingService trainingService;

    private GymFacade gymFacade;

    @BeforeEach
    void setUp() {
        traineeService = mock(TraineeService.class);
        trainerService = mock(TrainerService.class);
        trainingService = mock(TrainingService.class);

        gymFacade = new GymFacade(traineeService, trainerService, trainingService);
    }

    @Test
    void createTrainee_shouldDelegateToService() {
        String firstName = "Jane";
        String lastName = "Doe";
        LocalDate dateOfBirth = LocalDate.of(1995, 6, 15);
        String address =  "123 Main Street";
        gymFacade.createTrainee(firstName, lastName, dateOfBirth, address);

        verify(traineeService).createTrainee(firstName, lastName, dateOfBirth, address);
    }

    @Test
    void getTraineeById_shouldReturnTraineeFromService() {
        final var id = UUID.randomUUID();
        final var expected = new Trainee();
        when(traineeService.getTraineeById(id)).thenReturn(expected);
        Trainee actual = gymFacade.getTraineeById(id);

        assertSame(expected, actual);
        verify(traineeService).getTraineeById(id);
    }

    @Test
    void deleteTrainee_shouldDelegateToService() {
        final var id = UUID.randomUUID();
        gymFacade.deleteTrainee(id);

        verify(traineeService).deleteTrainee(id);
    }

    @Test
    void updateTrainee_shouldReturnUpdatedTraineeFromService() {
        final var trainee = new Trainee();
        final var expected = new Trainee();
        when(traineeService.updateTrainee(trainee)).thenReturn(expected);
        Trainee actual = gymFacade.updateTrainee(trainee);

        assertSame(expected, actual);
        verify(traineeService).updateTrainee(trainee);
    }

    @Test
    void createTrainer_shouldDelegateToService() {
        String firstName = "John";
        String lastName = "Smith";
        final var specialization = TrainingType.YOGA;
        gymFacade.createTrainer(firstName, lastName, specialization);

        verify(trainerService).createTrainer(firstName, lastName, specialization);
    }

    @Test
    void getTrainerById_shouldReturnTrainerFromService() {
        final var id = UUID.randomUUID();
        final var expected = new Trainer();
        when(trainerService.getTrainerById(id)).thenReturn(expected);
        Trainer actual = gymFacade.getTrainerById(id);

        assertSame(expected, actual);
        verify(trainerService).getTrainerById(id);
    }

    @Test
    void updateTrainer_shouldReturnUpdatedTrainerFromService() {
        final var trainer = new Trainer();
        final var expected = new Trainer();
        when(trainerService.updateTrainer(trainer)).thenReturn(expected);
        Trainer actual = gymFacade.updateTrainer(trainer);

        assertSame(expected, actual);
        verify(trainerService).updateTrainer(trainer);
    }

    @Test
    void createTraining_shouldDelegateToService() {
        final var training = new Training();
        gymFacade.createTraining(training);

        verify(trainingService).createTraining(training);
    }

    @Test
    void getTrainingById_shouldReturnTrainingFromService() {
        final var id = UUID.randomUUID();
        final var expected = new Training();
        when(trainingService.getTrainingById(id)).thenReturn(expected);
        Training actual = gymFacade.getTrainingById(id);

        assertSame(expected, actual);
        verify(trainingService).getTrainingById(id);
    }
}
