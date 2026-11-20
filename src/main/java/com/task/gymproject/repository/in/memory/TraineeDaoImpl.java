package com.task.gymproject.repository.in.memory;

import com.task.gymproject.model.Trainee;
import com.task.gymproject.repository.api.TraineeDao;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
@AllArgsConstructor
public class TraineeDaoImpl implements TraineeDao {

    private Map<UUID, Trainee> traineeStorage;

    @Override
    public void save(final Trainee trainee) {
        traineeStorage.put(trainee.getId(), trainee);
    }

    @Override
    public Optional<Trainee> findById(final UUID id) {
        return Optional.ofNullable(traineeStorage.get(id));
    }

    @Override
    public List<Trainee> findAll() {
        return new ArrayList<>(traineeStorage.values());
    }

    @Override
    public Trainee update(final Trainee trainee) {
        traineeStorage.put(trainee.getId(), trainee);

        return trainee;
    }

    @Override
    public void delete(final UUID id) {
        traineeStorage.remove(id);
    }

    @Override
    public boolean existsByUsername(final String username) {
        return traineeStorage.values().stream()
                .anyMatch(trainee -> trainee.getUsername().equals(username));
    }
}
