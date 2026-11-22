package com.task.gymproject.repository.api;

import com.task.gymproject.model.Training;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TrainingDao {

    void save(final Training training);

    Optional<Training> findById(final UUID id);

    Optional<Training> findByTrainingName(final String name);

    List<Training> findAll();

    Training update(final Training training);

    void delete(final UUID id);
}
