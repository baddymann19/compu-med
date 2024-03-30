package com.compu.patient.model;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PatientRequestDTO {

    @NotBlank(message="name may not be blank")
    private String name;
    @NotBlank(message="surname may not be blank")
    private String surname;
    @NotNull(message="dateOfBirth may not be blank")
    private LocalDate dateOfBirth;
    @NotBlank(message="socialSecurityNumber may not be blank")
    private String socialSecurityNumber;
}
