package com.task.gymproject.storage;

import com.task.gymproject.model.Trainee;
import com.task.gymproject.model.Trainer;
import com.task.gymproject.model.Training;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Configuration
public class StorageConfig {

    @Bean
    public Map<UUID, Trainee> traineeStorage() {
        return new ConcurrentHashMap<>();
    }

    @Bean
    public Map<UUID, Trainer> trainerStorage() {
        return new ConcurrentHashMap<>();
    }

    @Bean
    public Map<UUID, Training> trainingStorage() {
        return new ConcurrentHashMap<>();
    }

}
