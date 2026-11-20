package com.task.gymproject.storage;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.task.gymproject.model.Trainee;
import com.task.gymproject.model.Trainer;
import com.task.gymproject.model.Training;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanInitializationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.cglib.core.internal.Function;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Component
public class StorageInitializer implements BeanPostProcessor {

    private static final Logger log = LoggerFactory.getLogger(StorageInitializer.class);

    @Value("${storage.files.trainees}")
    private Resource traineesFile;

    @Value("${storage.files.trainers}")
    private Resource trainersFile;

    @Value("${storage.files.trainings}")
    private Resource trainingsFile;

    private final ObjectMapper objectMapper = new ObjectMapper()
            .registerModule(new JavaTimeModule());

    private List<Trainee> trainees;
    private List<Trainer> trainers;
    private List<Training> trainings;

    private boolean dataLoaded = false;

    private void loadData() {
        log.info("Loading initial storage data from JSON files");
        try {
            trainees = readJson(traineesFile, Trainee.class);
            trainers = readJson(trainersFile, Trainer.class);
            trainings = readJson(trainingsFile, Training.class);
            dataLoaded = true;
            log.info("Initial storage data loaded successfully");
        } catch (IOException e) {
            throw new BeanInitializationException(
                    "Failed to load initial data from JSON files", e
            );
        }
    }

    private <T> List<T> readJson(final Resource resource, final Class<T> type) throws IOException {
        try (final var inputStream = resource.getInputStream()) {
            final var javaType = objectMapper.getTypeFactory()
                    .constructCollectionType(List.class, type);

            return objectMapper.readValue(inputStream, javaType);
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public Object postProcessAfterInitialization(final Object bean, final String beanName) throws BeansException {
        if (!isStorageBean(beanName)) {
            return bean;
        }
        if (!dataLoaded) {
            loadData();
        }
        switch (beanName) {
            case "traineeStorage" ->
                    populate((Map<UUID, Trainee>) bean, trainees, Trainee::getId);
            case "trainerStorage" ->
                    populate((Map<UUID, Trainer>) bean, trainers, Trainer::getId);
            case "trainingStorage" ->
                    populate((Map<UUID, Training>) bean, trainings, Training::getId);
        }
        log.info("Initialized {} with {} entries", beanName, ((Map<?, ?>) bean).size());

        return bean;
    }

    private boolean isStorageBean(final String beanName) {
        return beanName.equals("traineeStorage")
                || beanName.equals("trainerStorage")
                || beanName.equals("trainingStorage");
    }

    private <T> void populate(final Map<UUID, T> storage, final List<T> entities, final Function<T, UUID> idExtractor) {
        for (final var  entity : entities) {
            final var id = idExtractor.apply(entity);
            if (id == null) {
                throw new BeanInitializationException(
                        "Entity ID cannot be null"
                );
            }
            if (storage.putIfAbsent(id, entity) != null) {
                throw new BeanInitializationException(
                        "Duplicate entity ID: " + id
                );
            }
        }
    }
}
