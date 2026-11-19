package com.task.gymproject.repository;

import com.task.gymproject.model.Trainee;
import com.task.gymproject.repository.in.memory.TraineeDaoImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class TraineeDaoImplTest {

    private Map<UUID, Trainee> traineeStorage;

    private TraineeDaoImpl traineeDAO;

    @BeforeEach
    void setUp() {
        traineeStorage = new HashMap<>();

        traineeDAO = new TraineeDaoImpl(traineeStorage);
    }

    @Test
    void save_shouldSaveTrainee() {
        final var userId = UUID.randomUUID();
        final var dateOfBirth = LocalDate.of(1995, 6, 15);
        final var trainee = new Trainee();
        trainee.setId(userId);
        trainee.setFirstName("Jane");
        trainee.setLastName("Doe");
        trainee.setDateOfBirth(dateOfBirth);
        trainee.setAddress("123 Main Street");
        traineeDAO.save(trainee);

        assertEquals(1, traineeStorage.size());
        assertTrue(traineeStorage.containsKey(userId));
        final var savedTrainee = traineeStorage.get(userId);
        assertSame(trainee, savedTrainee);
        assertEquals(userId, savedTrainee.getId());
        assertEquals("Jane", savedTrainee.getFirstName());
        assertEquals("Doe", savedTrainee.getLastName());
        assertEquals(dateOfBirth, savedTrainee.getDateOfBirth());
        assertEquals("123 Main Street", savedTrainee.getAddress());
    }

    @Test
    void findById_shouldReturnTrainee_whenExists() {
        final var id = UUID.randomUUID();
        final var trainee = new Trainee();
        trainee.setId(id);
        traineeStorage.put(id, trainee);
        final var result = traineeDAO.findById(id);

        assertEquals(Optional.of(trainee), result);
    }

    @Test
    void findById_shouldReturnEmpty_whenNotExists() {
        final var result = traineeDAO.findById(UUID.randomUUID());
        assertTrue(result.isEmpty());
    }

    @Test
    void findAll_shouldReturnAllTrainees() {
        final var trainee1 = new Trainee();
        trainee1.setId(UUID.randomUUID());
        final var trainee2 = new Trainee();
        trainee2.setId(UUID.randomUUID());
        traineeStorage.put(trainee1.getId(), trainee1);
        traineeStorage.put(trainee2.getId(), trainee2);
        final var result = traineeDAO.findAll();

        assertEquals(2, result.size());
        assertTrue(result.containsAll(List.of(trainee1, trainee2)));
    }

    @Test
    void findAll_shouldReturnEmptyList_whenStorageIsEmpty() {
        final var result = traineeDAO.findAll();

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void delete_shouldRemoveTrainee() {
        final var traineeId = UUID.randomUUID();
        final var trainee = new Trainee();
        trainee.setId(traineeId);
        traineeStorage.put(traineeId, trainee);
        traineeDAO.delete(traineeId);

        assertFalse(traineeStorage.containsKey(traineeId));
        assertTrue(traineeStorage.isEmpty());
    }

    @Test
    void update_shouldReplaceExistingTrainee() {
        final var traineeId = UUID.randomUUID();
        final var oldTrainee = new Trainee();
        oldTrainee.setId(traineeId);
        traineeStorage.put(traineeId, oldTrainee);
        final var updatedTrainee = new Trainee();
        updatedTrainee.setId(traineeId);
        final var result = traineeDAO.update(updatedTrainee);

        assertSame(updatedTrainee, result);
        assertSame(updatedTrainee, traineeStorage.get(traineeId));
        assertEquals(1, traineeStorage.size());
    }

    @Test
    void existsByUsername_shouldReturnTrueWhenUsernameExists() {
        final var trainee = new Trainee();
        trainee.setId(UUID.randomUUID());
        trainee.setUsername("Jane.Doe");
        traineeStorage.put(trainee.getId(), trainee);
        final var result = traineeDAO.existsByUsername("Jane.Doe");

        assertTrue(result);
    }

    @Test
    void existsByUsername_shouldReturnFalseWhenUsernameDoesNotExist() {
        final var trainee = new Trainee();
        trainee.setId(UUID.randomUUID());
        trainee.setUsername("Jane.Doe");
        traineeStorage.put(trainee.getId(), trainee);
        final var result = traineeDAO.existsByUsername("John.Doe");

        assertFalse(result);
    }

    @Test
    void existsByUsername_shouldReturnFalseWhenStorageIsEmpty() {
        final var result = traineeDAO.existsByUsername("Jane.Doe");

        assertFalse(result);
    }
}
