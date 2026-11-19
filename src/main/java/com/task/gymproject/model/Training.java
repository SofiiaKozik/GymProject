package com.task.gymproject.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Training {

    private UUID id;

    private UUID traineeId;

    private UUID trainerId;

    private String trainingName;

    private TrainingType trainingType;

    private LocalDate trainingDate;

    private int duration;

}
