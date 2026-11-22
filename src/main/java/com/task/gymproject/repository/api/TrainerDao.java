package com.task.gymproject.repository.api;

import com.task.gymproject.model.Trainer;
import com.task.gymproject.model.TrainingType;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TrainerDao {

    void save(final Trainer trainer);

    Optional<Trainer> findById(final UUID id);

    List<Trainer> findAll();

    Trainer update(final Trainer trainer);

    void delete(final UUID id);

    boolean existsByUsername(final String username);

}
