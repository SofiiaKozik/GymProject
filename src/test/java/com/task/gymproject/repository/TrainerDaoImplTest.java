package com.task.gymproject.repository;

import com.task.gymproject.model.Trainer;
import com.task.gymproject.model.TrainingType;
import com.task.gymproject.repository.in.memory.TrainerDaoImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class TrainerDaoImplTest {

    private Map<UUID, Trainer> trainerStorage;

    private TrainerDaoImpl trainerDAO;

    @BeforeEach
    void setUp() {
        trainerStorage = new HashMap<>();

        trainerDAO = new TrainerDaoImpl(trainerStorage);
    }

    @Test
    void save_shouldSaveTrainer() {
        final var userId = UUID.randomUUID();
        final var trainer = new Trainer();
        trainer.setId(userId);
        trainer.setFirstName("Jane");
        trainer.setLastName("Doe");
        trainer.setSpecialization(TrainingType.FITNESS);
        trainerDAO.save(trainer);
        assertEquals(1, trainerStorage.size());
        assertTrue(trainerStorage.containsKey(userId));
        final var savedTrainer = trainerStorage.get(userId);

        assertSame(trainer, savedTrainer);
        assertEquals(userId, savedTrainer.getId());
        assertEquals("Jane", savedTrainer.getFirstName());
        assertEquals("Doe", savedTrainer.getLastName());
        assertEquals(TrainingType.FITNESS, savedTrainer.getSpecialization());
    }

    @Test
    void findById_shouldReturnTrainer_whenExists() {
        final var id = UUID.randomUUID();
        final var trainer = new Trainer();
        trainer.setId(id);
        trainerStorage.put(id, trainer);
        final var result = trainerDAO.findById(id);

        assertTrue(result.isPresent());
        assertEquals(trainer, result.get());
    }

    @Test
    void findById_shouldReturnEmpty_whenNotExists() {
        final var result = trainerDAO.findById(UUID.randomUUID());

        assertTrue(result.isEmpty());
    }

    @Test
    void findAll_shouldReturnAllTrainers() {
        final var trainer1 = new Trainer();
        trainer1.setId(UUID.randomUUID());
        final var trainer2 = new Trainer();
        trainer2.setId(UUID.randomUUID());
        trainerStorage.put(trainer1.getId(), trainer1);
        trainerStorage.put(trainer2.getId(), trainer2);
        final var result = trainerDAO.findAll();

        assertEquals(2, result.size());
        assertTrue(result.contains(trainer1));
        assertTrue(result.contains(trainer2));
    }

    @Test
    void  findAll_shouldReturnEmpty_whenStorageIsEmpty() {
        final var result = trainerDAO.findAll();

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void delete_shouldRemoveTrainer() {
        final var trainerId = UUID.randomUUID();
        final var trainer = new Trainer();
        trainer.setId(trainerId);
        trainerStorage.put(trainerId, trainer);
        trainerDAO.delete(trainerId);

        assertFalse(trainerStorage.containsKey(trainerId));
        assertTrue(trainerStorage.isEmpty());
    }

    @Test
    void update_shouldReplaceExistingTrainer() {
        final var trainerId = UUID.randomUUID();
        final var oldTrainer = new Trainer();
        oldTrainer.setId(trainerId);
        trainerStorage.put(trainerId, oldTrainer);
        final var updatedTrainer = new Trainer();
        updatedTrainer.setId(trainerId);
        final var result = trainerDAO.update(updatedTrainer);

        assertSame(updatedTrainer, result);
        assertSame(updatedTrainer, trainerStorage.get(trainerId));
        assertEquals(1, trainerStorage.size());
    }

    @Test
    void existsByUsername_shouldReturnTrueWhenUsernameExists() {
        final var trainer = new Trainer();
        trainer.setId(UUID.randomUUID());
        trainer.setUsername("Jane.Doe");
        trainerStorage.put(trainer.getId(), trainer);
        final var result = trainerDAO.existsByUsername("Jane.Doe");

        assertTrue(result);
    }

    @Test
    void existsByUsername_shouldReturnFalseWhenUsernameDoesNotExist() {
        final var trainer = new Trainer();
        trainer.setId(UUID.randomUUID());
        trainer.setUsername("Jane.Doe");
        trainerStorage.put(trainer.getId(), trainer);
        final var result = trainerDAO.existsByUsername("John.Doe");

        assertFalse(result);
    }

    @Test
    void existsByUsername_shouldReturnFalseWhenStorageIsEmpty() {
        final var result = trainerDAO.existsByUsername("Jane.Doe");

        assertFalse(result);
    }
}
