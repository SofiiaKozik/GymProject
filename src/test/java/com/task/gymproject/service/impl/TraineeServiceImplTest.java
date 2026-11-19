package com.task.gymproject.service.impl;

import com.task.gymproject.exeption.EntityNotFoundExeption;
import com.task.gymproject.model.Trainee;
import com.task.gymproject.repository.api.TraineeDao;
import com.task.gymproject.service.api.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class TraineeServiceImplTest {

    @Mock
    private TraineeDao traineeDAO;

    @Mock
    private UserService userService;

    private TraineeServiceImpl traineeService;

    @BeforeEach
    void setUp() {
        traineeDAO = mock(TraineeDao.class);
        userService = mock(UserService.class);

        traineeService = new TraineeServiceImpl(traineeDAO, userService);
    }

    @Test
    void createTrainee() {
        String firstName = "Jane";
        String lastName = "Doe";
        final var dateOfBirth = LocalDate.of(1995, 6, 15);
        String address = "123 Main Street";
        traineeService.createTrainee(firstName, lastName, dateOfBirth, address);

        verify(traineeDAO).save(any(Trainee.class));
    }

    @Test
    void getTraineeByIdThrowsWhenTraineeDoesNotExist() {
        final var id = UUID.randomUUID();
        when(traineeDAO.findById(id)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundExeption.class, () -> traineeService.getTraineeById(id));
    }

    @Test
    void getTraineeByIdWhenTraineeExists() {
        final var id = UUID.randomUUID();
        final var trainee = new Trainee();
        trainee.setId(id);
        when(traineeDAO.findById(id)).thenReturn(Optional.of(trainee));

        assertSame(trainee, traineeService.getTraineeById(id));
        verify(traineeDAO).findById(id);
    }

    @Test
    void updateTraineeDoesNotUpdateWhenTraineeDoesNotExist() {
        final var id = UUID.randomUUID();
        final var trainee = new Trainee();
        trainee.setId(id);
        when(traineeDAO.findById(id)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundExeption.class, () -> traineeService.updateTrainee(trainee));
        verify(traineeDAO, never()).update(any());
    }

    @Test
    void updateTraineeWhenTraineeExists() {
        final var id = UUID.randomUUID();
        final var trainee = new Trainee();
        trainee.setId(id);
        final var updatedTrainee = new Trainee();
        updatedTrainee.setId(id);
        when(traineeDAO.findById(id)).thenReturn(Optional.of(trainee));
        when(traineeDAO.update(trainee)).thenReturn(updatedTrainee);
        final var result = traineeService.updateTrainee(trainee);

        assertSame(updatedTrainee, result);
        verify(traineeDAO).findById(id);
        verify(traineeDAO).update(trainee);
    }

    @Test
    void deleteTraineeDoesNotDeleteWhenTraineeDoesNotExist() {
        final var id = UUID.randomUUID();
        when(traineeDAO.findById(id)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundExeption.class, () -> traineeService.deleteTrainee(id));
        verify(traineeDAO, never()).delete(id);
    }

    @Test
    void deleteTraineeWhenTraineeExists() {
        final var id = UUID.randomUUID();
        final var trainee = new Trainee();
        trainee.setId(id);

        when(traineeDAO.findById(id)).thenReturn(Optional.of(trainee));

        traineeService.deleteTrainee(id);

        verify(traineeDAO).findById(id);
        verify(traineeDAO).delete(id);
    }

    @Test
    void shouldThrowExceptionWhenTraineeIdIsNull() {
        final var trainee = new Trainee();
        trainee.setId(null);
        final var exception = assertThrows(IllegalArgumentException.class, () -> traineeService.updateTrainee(trainee));

        assertEquals("Trainee ID cannot be null for update.", exception.getMessage());
        verifyNoInteractions(traineeDAO, userService);
    }
}
