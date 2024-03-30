package com.compu.patient.model;

import io.quarkus.hibernate.reactive.panache.PanacheEntityBase;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Patient extends PanacheEntityBase {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long id;
    private String name;
    private String surname;
    private LocalDate dateOfBirth;
    private String socialSecurityNumber;
}
