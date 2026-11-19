package com.task.gymproject.service.impl;

import com.task.gymproject.generator.PasswordGenerator;
import com.task.gymproject.model.Trainee;
import com.task.gymproject.model.Trainer;
import com.task.gymproject.model.User;
import com.task.gymproject.repository.api.TraineeDao;
import com.task.gymproject.repository.api.TrainerDao;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserServiceImplTest {

    private PasswordGenerator passwordGenerator;

    private TraineeDao traineeDAO;

    private TrainerDao trainerDAO;

    private UserServiceImpl userService;

    @BeforeEach
    void setUp() {
        passwordGenerator = mock(PasswordGenerator.class);
        traineeDAO = mock(TraineeDao.class);
        trainerDAO = mock(TrainerDao.class);

        userService = new UserServiceImpl(passwordGenerator, traineeDAO, trainerDAO);

    }

    @Test
    void shouldInitializeTrainer() {
        final var trainer = new Trainer();
        when(passwordGenerator.generate()).thenReturn("password123");
        userService.initializeUser(trainer, "John", "Smith");

        assertNotNull(trainer.getId());
        assertEquals("John", trainer.getFirstName());
        assertEquals("Smith", trainer.getLastName());
        assertEquals("John.Smith", trainer.getUsername());
        assertEquals("password123", trainer.getPassword());
        assertTrue(trainer.isActive());
        verify(passwordGenerator).generate();
        verify(traineeDAO).existsByUsername("John.Smith");
    }

    @Test
    void shouldInitializeTrainee() {
        final var trainee = new Trainee();
        when(passwordGenerator.generate()).thenReturn("password456");
        userService.initializeUser(trainee, "Jane", "Doe");

        assertNotNull(trainee.getId());
        assertEquals("Jane", trainee.getFirstName());
        assertEquals("Doe", trainee.getLastName());
        assertEquals("Jane.Doe", trainee.getUsername());
        assertEquals("password456", trainee.getPassword());
        assertTrue(trainee.isActive());
        verify(passwordGenerator).generate();
        verify(traineeDAO).existsByUsername("Jane.Doe");
    }

    @Test
    void shouldGenerateNextAvailableUsername() {
        when(traineeDAO.existsByUsername("John.Smith")).thenReturn(true);
        when(traineeDAO.existsByUsername("John.Smith1")).thenReturn(true);
        when(traineeDAO.existsByUsername("John.Smith2")).thenReturn(false);
        final var trainer = new Trainer();
        when(passwordGenerator.generate()).thenReturn("password123");
        userService.initializeUser(trainer, "John", "Smith");

        assertEquals("John.Smith2", trainer.getUsername());
        verify(traineeDAO).existsByUsername("John.Smith");
        verify(traineeDAO).existsByUsername("John.Smith1");
        verify(traineeDAO).existsByUsername("John.Smith2");
    }

    @Test
    void shouldReturnTrueWhenUsernameExistsInTrainerStorage() {
        final var user = new User();

        when(traineeDAO.existsByUsername("John.Smith")).thenReturn(false);
        when(trainerDAO.existsByUsername("John.Smith")).thenReturn(true);
        when(traineeDAO.existsByUsername("John.Smith1")).thenReturn(false);
        when(trainerDAO.existsByUsername("John.Smith1")).thenReturn(false);
        userService.initializeUser(user, "John", "Smith");

        assertEquals("John.Smith1", user.getUsername());
        verify(traineeDAO).existsByUsername("John.Smith");
        verify(trainerDAO).existsByUsername("John.Smith");
        verify(traineeDAO).existsByUsername("John.Smith1");
        verify(trainerDAO).existsByUsername("John.Smith1");
    }

    @Test
    void shouldGenerateUsernameWithSuffixWhenUsernameExistsInTrainerStorage() {
        final var user = new User();
        when(traineeDAO.existsByUsername("John.Smith")).thenReturn(false);
        when(trainerDAO.existsByUsername("John.Smith")).thenReturn(true);
        when(traineeDAO.existsByUsername("John.Smith1")).thenReturn(false);
        when(trainerDAO.existsByUsername("John.Smith1")).thenReturn(false);
        userService.initializeUser(user, "John", "Smith");

        assertEquals("John.Smith1", user.getUsername());
        verify(traineeDAO).existsByUsername("John.Smith");
        verify(trainerDAO).existsByUsername("John.Smith");
        verify(traineeDAO).existsByUsername("John.Smith1");
        verify(trainerDAO).existsByUsername("John.Smith1");
    }

}
