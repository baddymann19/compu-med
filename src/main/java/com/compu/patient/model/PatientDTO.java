package com.compu.patient.model;


import lombok.*;

import java.time.LocalDate;
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class PatientDTO {

    private Long id;

    private String name;
    private String surname;
    private LocalDate dateOfBirth;
    private String socialSecurityNumber;
}
