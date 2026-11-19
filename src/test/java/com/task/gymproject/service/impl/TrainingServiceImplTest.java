package com.task.gymproject.service.impl;

import com.task.gymproject.repository.api.TrainingDao;
import com.task.gymproject.exeption.EntityNotFoundExeption;
import com.task.gymproject.model.Training;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class TrainingServiceImplTest {

    @Mock
    private TrainingDao trainingDAO;

    private TrainingServiceImpl trainingService;

    @BeforeEach
    void setUp() {
        trainingDAO = mock(TrainingDao.class);

        trainingService = new TrainingServiceImpl(trainingDAO);
    }

    @Test
    void createTraining() {
        final var training = new Training();
        training.setId(UUID.randomUUID());
        trainingService.createTraining(training);

        verify(trainingDAO).save(training);
    }

    @Test
    void getTrainingByIdThrowsWhenTrainingDoesNotExist() {
        final var id = UUID.randomUUID();
        when(trainingDAO.findById(id)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundExeption.class, () -> trainingService.getTrainingById(id));
    }

    @Test
    void getTrainingByIdWhenTrainingExists() {
        final var id = UUID.randomUUID();
        final var training = new Training();
        training.setId(id);
        when(trainingDAO.findById(id)).thenReturn(Optional.of(training));

        Assertions.assertSame(training, trainingService.getTrainingById(id));
        verify(trainingDAO).findById(id);
    }
}
