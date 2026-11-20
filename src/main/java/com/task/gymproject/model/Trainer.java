package com.task.gymproject.model;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Trainer extends  User{

    private TrainingType specialization;

}
