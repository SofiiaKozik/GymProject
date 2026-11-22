package com.task.gymproject.repository;

import com.task.gymproject.repository.in.memory.TrainingDaoImpl;
import com.task.gymproject.model.Training;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class TrainingDaoImplTest {
    private Map<UUID, Training> trainingStorage;

    private TrainingDaoImpl trainingDAO;

    @BeforeEach
    void setUp() {
        trainingStorage = new HashMap<>();

        trainingDAO = new TrainingDaoImpl(trainingStorage);
    }

    @Test
    void save_shouldSaveTraining() {
        final var id = UUID.randomUUID();
        final var training = new Training();
        training.setId(id);
        trainingDAO.save(training);

        assertEquals(training, trainingStorage.get(id));
        assertEquals(1, trainingStorage.size());
    }

    @Test
    void findById_shouldReturnTraining_whenExists() {
        final var id = UUID.randomUUID();
        final var training = new Training();
        training.setId(id);
        trainingStorage.put(id, training);
        final var result = trainingDAO.findById(id);

        assertTrue(result.isPresent());
        assertEquals(training, result.get());
    }

    @Test
    void findById_shouldReturnEmpty_whenNotExists() {
        final var id = UUID.randomUUID();
        final var result = trainingDAO.findById(id);

        assertTrue(result.isEmpty());
    }

    @Test
    void findAll_shouldReturnAllTrainings() {
        final var training1 = new Training();
        training1.setId(UUID.randomUUID());
        final var training2 = new Training();
        training2.setId(UUID.randomUUID());
        trainingStorage.put(training1.getId(), training1);
        trainingStorage.put(training2.getId(), training2);
        final var result = trainingDAO.findAll();

        assertEquals(2, result.size());
        assertTrue(result.contains(training1));
        assertTrue(result.contains(training2));
    }

    @Test
    void findAll_shouldReturnEmpty_whenStorageIsEmpty() {
        final var result = trainingDAO.findAll();

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void update_shouldReplaceExistingTraining() {
        final var id = UUID.randomUUID();
        final var oldTraining = new Training();
        oldTraining.setId(id);
        final var updatedTraining = new Training();
        updatedTraining.setId(id);
        trainingStorage.put(id, oldTraining);
        final var result = trainingDAO.update(updatedTraining);

        assertEquals(updatedTraining, result);
        assertEquals(updatedTraining, trainingStorage.get(id));
        assertEquals(1, trainingStorage.size());
    }

    @Test
    void delete_shouldRemoveTraining() {
        final var id = UUID.randomUUID();
        final var training = new Training();
        training.setId(id);
        trainingStorage.put(id, training);
        trainingDAO.delete(id);

        assertFalse(trainingStorage.containsKey(id));
        assertTrue(trainingStorage.isEmpty());
    }

    @Test
    void findByTrainingName_shouldReturnTraining_whenTrainingNameExists() {
        final var trainingId = UUID.randomUUID();
        final var trainingName = "testtraining";
        final var training = new Training();
        training.setId(trainingId);
        training.setTrainingName(trainingName);
        trainingStorage.put(trainingId, training);
        final var result = trainingDAO.findByTrainingName(trainingName);

        assertTrue(result.isPresent());
        assertEquals(training, result.get());
    }

    @Test
    void findByTrainingName_shouldReturnEmpty_whenTrainingNameDoesNotExist() {
        final var trainingId = UUID.randomUUID();
        final var trainingName = "testtraining";
        final var training = new Training();
        training.setId(trainingId);
        training.setTrainingName(trainingName);
        trainingStorage.put(trainingId, training);
        final var result = trainingDAO.findByTrainingName("unknown");

        assertTrue(result.isEmpty());
    }
}
