package com.task.gymproject.service.impl;

import com.task.gymproject.exeption.EntityNotFoundExeption;
import com.task.gymproject.model.Trainer;
import com.task.gymproject.model.TrainingType;
import com.task.gymproject.repository.api.TrainerDao;
import com.task.gymproject.service.api.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class TrainerServiceImplTest {

    @Mock
    private TrainerDao trainerDAO;

    @Mock
    private UserService userService;

    private TrainerServiceImpl trainerService;

    @BeforeEach
    void setUp() {
        trainerDAO = mock(TrainerDao.class);
        userService = mock(UserService.class);

        trainerService = new TrainerServiceImpl(trainerDAO, userService);
    }

    @Test
    void createTrainer() {
        String firstName = "Jane";
        String lastName = "Doe";
        final var specialization = TrainingType.BARRE;
        trainerService.createTrainer(firstName, lastName, specialization);

        verify(trainerDAO).save(any(Trainer.class));
    }

    @Test
    void getTrainerByIdThrowsWhenTrainerDoesNotExist() {
        final var id = UUID.randomUUID();
        when(trainerDAO.findById(id)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundExeption.class, () -> trainerService.getTrainerById(id));
    }

    @Test
    void getTrainerByIdWhenTrainerExists() {
        final var id = UUID.randomUUID();
        final var trainer = new Trainer();
        trainer.setId(id);
        when(trainerDAO.findById(id)).thenReturn(Optional.of(trainer));

        assertSame(trainer, trainerService.getTrainerById(id));
        verify(trainerDAO).findById(id);
    }

    @Test
    void updateTrainerDoesNotUpdateWhenTrainerDoesNotExist() {
        final var id = UUID.randomUUID();
        final var trainer = new Trainer();
        trainer.setId(id);
        when(trainerDAO.findById(id)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundExeption.class, () -> trainerService.updateTrainer(trainer));
        verify(trainerDAO, never()).update(any());
    }

    @Test
    void updateTrainerWhenTrainerExists() {
        final var id = UUID.randomUUID();
        final var trainer = new Trainer();
        trainer.setId(id);
        final var updatedTrainer = new Trainer();
        updatedTrainer.setId(id);
        when(trainerDAO.findById(id)).thenReturn(Optional.of(trainer));
        when(trainerDAO.update(trainer)).thenReturn(updatedTrainer);
        final var result = trainerService.updateTrainer(trainer);
        assertSame(updatedTrainer, result);

        verify(trainerDAO).findById(id);
        verify(trainerDAO).update(trainer);
    }

    @Test
    void shouldThrowExceptionWhenTrainerIdIsNull() {
        final var trainer = new Trainer();
        trainer.setId(null);
        final var exception = assertThrows(IllegalArgumentException.class, () -> trainerService.updateTrainer(trainer));

        assertEquals("Trainer ID cannot be null for update.", exception.getMessage());
        verifyNoInteractions(trainerDAO, userService);
    }
}
