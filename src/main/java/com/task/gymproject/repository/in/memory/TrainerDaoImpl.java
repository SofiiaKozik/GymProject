package com.task.gymproject.repository.in.memory;

import com.task.gymproject.model.Trainer;
import com.task.gymproject.repository.api.TrainerDao;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
@AllArgsConstructor
public class TrainerDaoImpl implements TrainerDao {

    private Map<UUID, Trainer> trainerStorage;

    @Override
    public void save(final Trainer trainer) {
        trainerStorage.put(trainer.getId(), trainer);
    }

    @Override
    public Optional<Trainer> findById(final UUID id) {
        return Optional.ofNullable(trainerStorage.get(id));
    }

    @Override
    public List<Trainer> findAll() {
        return new ArrayList<>(trainerStorage.values());
    }

    @Override
    public Trainer update(final Trainer trainer) {
        trainerStorage.put(trainer.getId(), trainer);

        return trainer;
    }

    @Override
    public void delete(final UUID id) {
        trainerStorage.remove(id);
    }

    @Override
    public boolean existsByUsername(final String username) {
        return trainerStorage.values().stream()
                .anyMatch(trainer -> trainer.getUsername().equals(username));
    }
}
