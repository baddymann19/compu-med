package com.compu.visit.repo;

import com.compu.visit.model.Visit;
import io.quarkus.hibernate.reactive.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class PatientRepository implements PanacheRepositoryBase<Visit, Long> {

}
