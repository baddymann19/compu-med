package com.compu.patient.model;


import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PatientRequestDTO {

    private String name;
    private String surname;
    private LocalDate dateOfBirth;
    private String socialSecurityNumber;
}
