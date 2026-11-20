package com.task.gymproject.repository.api;

import com.task.gymproject.model.Trainee;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TraineeDao {

    void save(final Trainee trainee);

    Optional<Trainee> findById(final UUID id);

    List<Trainee> findAll();

    Trainee update(final Trainee trainee);

    void delete(final UUID id);

    boolean existsByUsername(final String username);
}
