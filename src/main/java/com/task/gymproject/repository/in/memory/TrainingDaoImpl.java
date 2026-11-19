package com.task.gymproject.repository.in.memory;

import com.task.gymproject.model.Training;
import com.task.gymproject.repository.api.TrainingDao;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
@AllArgsConstructor
public class TrainingDaoImpl implements TrainingDao {

    private Map<UUID, Training> trainingStorage;

    @Override
    public void save(final Training training) {
        trainingStorage.put(training.getId(), training);
    }

    @Override
    public Optional<Training> findById(final UUID id) {
        return Optional.ofNullable(trainingStorage.get(id));
    }

    @Override
    public Optional<Training> findByTrainingName(final String name) {
        return trainingStorage.values().stream()
                .filter(training -> training.getTrainingName().equals(name))
                .findFirst();
    }

    @Override
    public List<Training> findAll() {
        return new ArrayList<>(trainingStorage.values());
    }

    @Override
    public Training update(final Training training) {
        trainingStorage.put(training.getId(), training);

        return training;
    }

    @Override
    public void delete(final UUID id) {
        trainingStorage.remove(id);

    }
}
