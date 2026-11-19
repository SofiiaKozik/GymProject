package com.task.gymproject.storage;

import com.task.gymproject.model.Trainee;
import com.task.gymproject.model.Trainer;
import com.task.gymproject.model.Training;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.BeanInitializationException;
import org.springframework.core.io.Resource;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class StorageInitializerTest {
    private StorageInitializer storageInitializer;

    private Resource traineesFile;
    private Resource trainersFile;
    private Resource trainingsFile;

    private static final UUID TRAINEE_ID =
            UUID.fromString("a1b2c3d4-e5f6-7890-abcd-ef1234567890");

    private static final UUID TRAINER_ID =
            UUID.fromString("b2c3d4e5-f6a7-8901-bcde-f12345678901");

    private static final UUID SECOND_TRAINER_ID =
            UUID.fromString("c3d4e5f6-a7b8-9012-cdef-123456789012");

    private static final UUID TRAINING_ID =
            UUID.fromString("11111111-2222-3333-4444-555555555555");

    private static final String TRAINEES_JSON = """
            [
              {
                "id": "a1b2c3d4-e5f6-7890-abcd-ef1234567890",
                "firstName": "John",
                "lastName": "Doe",
                "username": "john.doe",
                "password": "hashedpassword123",
                "active": true,
                "dateOfBirth": "1995-06-15",
                "address": "123 Main Street, New York, NY"
              }
            ]
            """;

    private static final String TRAINERS_JSON = """
            [
              {
                "id": "b2c3d4e5-f6a7-8901-bcde-f12345678901",
                "firstName": "Sarah",
                "lastName": "Smith",
                "username": "sarah.smith",
                "password": "hashedpassword456",
                "active": true,
                "specialization": "YOGA"
              },
              {
                "id": "c3d4e5f6-a7b8-9012-cdef-123456789012",
                "firstName": "Michael",
                "lastName": "Johnson",
                "username": "michael.j",
                "password": "hashedpassword789",
                "active": true,
                "specialization": "FITNESS"
              }
            ]
            """;

    private static final String TRAININGS_JSON = """
            [
              {
                "id": "11111111-2222-3333-4444-555555555555",
                "traineeId": "a1b2c3d4-e5f6-7890-abcd-ef1234567890",
                "trainerId": "b2c3d4e5-f6a7-8901-bcde-f12345678901",
                "trainingName": "Morning Flow Yoga",
                "trainingType": "YOGA",
                "trainingDate": "2026-06-10",
                "duration": 60
              }
            ]
            """;

    @BeforeEach
    void setUp() {
        storageInitializer = new StorageInitializer();
        traineesFile = mockResource(TRAINEES_JSON);
        trainersFile = mockResource(TRAINERS_JSON);
        trainingsFile = mockResource(TRAININGS_JSON);

        ReflectionTestUtils.setField(storageInitializer, "traineesFile", traineesFile);
        ReflectionTestUtils.setField(storageInitializer, "trainersFile", trainersFile);
        ReflectionTestUtils.setField(storageInitializer, "trainingsFile", trainingsFile);
    }

    private Resource mockResource(final String json) {
        Resource resource = mock(Resource.class);
        try {
            when(resource.getInputStream()).thenAnswer(invocation ->
                    new ByteArrayInputStream(json.getBytes(StandardCharsets.UTF_8)));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return resource;
    }

    @Test
    void shouldLoadAllDataIntoCorrespondingStorages() {
        Map<UUID, Trainee> traineeStorage = new ConcurrentHashMap<>();
        Map<UUID, Trainer> trainerStorage = new ConcurrentHashMap<>();
        Map<UUID, Training> trainingStorage = new ConcurrentHashMap<>();

        storageInitializer.postProcessAfterInitialization(traineeStorage, "traineeStorage");
        storageInitializer.postProcessAfterInitialization(trainerStorage, "trainerStorage");
        storageInitializer.postProcessAfterInitialization(trainingStorage, "trainingStorage");

        assertEquals(1, traineeStorage.size());
        assertTrue(traineeStorage.containsKey(TRAINEE_ID));

        final var trainee = traineeStorage.get(TRAINEE_ID);
        assertEquals("John", trainee.getFirstName());
        assertEquals("Doe", trainee.getLastName());
        assertEquals("john.doe", trainee.getUsername());
        assertEquals("hashedpassword123", trainee.getPassword());
        assertTrue(trainee.isActive());
        assertEquals("123 Main Street, New York, NY", trainee.getAddress());
        assertEquals(2, trainerStorage.size());
        assertTrue(trainerStorage.containsKey(TRAINER_ID));
        assertTrue(trainerStorage.containsKey(SECOND_TRAINER_ID));

        final var trainer = trainerStorage.get(TRAINER_ID);
        assertEquals("Sarah", trainer.getFirstName());
        assertEquals("sarah.smith", trainer.getUsername());
        assertEquals("YOGA", trainer.getSpecialization().name());
        assertEquals(1, trainingStorage.size());
        assertTrue(trainingStorage.containsKey(TRAINING_ID));

        final var training = trainingStorage.get(TRAINING_ID);
        assertEquals("Morning Flow Yoga", training.getTrainingName());
        assertEquals(TRAINEE_ID, training.getTraineeId());
        assertEquals(TRAINER_ID, training.getTrainerId());
    }

    @Test
    void shouldLoadDataOnlyOnce() throws IOException {
        Map<UUID, Trainee> traineeStorage = new ConcurrentHashMap<>();
        Map<UUID, Trainer> trainerStorage = new ConcurrentHashMap<>();
        Map<UUID, Training> trainingStorage = new ConcurrentHashMap<>();
        storageInitializer.postProcessAfterInitialization(traineeStorage, "traineeStorage");
        storageInitializer.postProcessAfterInitialization(trainerStorage, "trainerStorage");
        storageInitializer.postProcessAfterInitialization(trainingStorage, "trainingStorage");

        verify(traineesFile, times(1)).getInputStream();
        verify(trainersFile, times(1)).getInputStream();
        verify(trainingsFile, times(1)).getInputStream();
    }

    @Test
    void shouldIgnoreNonStorageBeans() {
        final var serviceBean = new Object();
        final var result = storageInitializer.postProcessAfterInitialization(serviceBean, "traineeService");

        assertSame(serviceBean, result);
        verifyNoInteractions(traineesFile, trainersFile, trainingsFile);
    }

    @Test
    void shouldThrowBeanInitializationExceptionWhenFileCannotBeRead() throws IOException {
        Resource brokenResource = mock(Resource.class);
        when(brokenResource.getInputStream()).thenThrow(new IOException("File not found"));
        ReflectionTestUtils.setField(storageInitializer, "traineesFile", brokenResource);
        Map<UUID, Trainee> traineeStorage = new ConcurrentHashMap<>();

        assertThrows(BeanInitializationException.class,
                () -> storageInitializer.postProcessAfterInitialization(traineeStorage, "traineeStorage"));
    }

    @Test
    void shouldThrowExceptionWhenEntityIdIsNull() {
        String invalidTraineesJson = """
                [
                  {
                    "firstName": "John",
                    "lastName": "Doe",
                    "username": "john.doe",
                    "password": "password123",
                    "active": true,
                    "dateOfBirth": "1995-06-15",
                    "address": "123 Main Street"
                  }
                ]
                """;

        ReflectionTestUtils.setField(storageInitializer, "traineesFile", mockResource(invalidTraineesJson));
        Map<UUID, Trainee> traineeStorage = new ConcurrentHashMap<>();

        assertThrows(BeanInitializationException.class,
                () -> storageInitializer.postProcessAfterInitialization(traineeStorage, "traineeStorage"));
    }

    @Test
    void shouldThrowExceptionWhenDuplicateEntityIdExists() {
        String duplicateTraineesJson = """
                [
                  {
                    "id": "a1b2c3d4-e5f6-7890-abcd-ef1234567890",
                    "firstName": "John",
                    "lastName": "Doe",
                    "username": "john.doe",
                    "password": "password123",
                    "active": true,
                    "dateOfBirth": "1995-06-15",
                    "address": "123 Main Street"
                  },
                  {
                    "id": "a1b2c3d4-e5f6-7890-abcd-ef1234567890",
                    "firstName": "Jane",
                    "lastName": "Doe",
                    "username": "jane.doe",
                    "password": "password456",
                    "active": true,
                    "dateOfBirth": "1998-04-20",
                    "address": "456 Main Street"
                  }
                ]
                """;

        ReflectionTestUtils.setField(storageInitializer, "traineesFile", mockResource(duplicateTraineesJson));
        Map<UUID, Trainee> traineeStorage = new ConcurrentHashMap<>();

        assertThrows(BeanInitializationException.class,
                () -> storageInitializer.postProcessAfterInitialization(traineeStorage, "traineeStorage"));
        assertEquals(1, traineeStorage.size());
        assertEquals("john.doe", traineeStorage.get(TRAINEE_ID).getUsername());
    }
}