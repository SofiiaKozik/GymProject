package com.task.gymproject.model;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Trainee extends User{

    private LocalDate dateOfBirth;

    private String address;

}
