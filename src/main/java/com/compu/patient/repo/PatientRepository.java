package com.compu.patient.repo;

import com.compu.patient.model.Patient;
import io.quarkus.hibernate.reactive.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class PatientRepository implements PanacheRepositoryBase<Patient, Long> {

}
